package sn.ucad.cotisations.servlet;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Cotisation;
import sn.ucad.cotisations.model.Membre;
import sn.ucad.cotisations.service.AmendeService;
import sn.ucad.cotisations.service.CotisationService;
import sn.ucad.cotisations.service.MembreService;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Export des listes (membres / cotisations / amendes) en PDF ou Excel.
 * URL : /admin/export?type=membres|cotisations|amendes&format=pdf|excel
 */
@WebServlet("/admin/export")
public class ExportServlet extends HttpServlet {

    private final MembreService     membreService     = new MembreService();
    private final CotisationService cotisationService = new CotisationService();
    private final AmendeService     amendeService     = new AmendeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String type   = req.getParameter("type");
        String format = req.getParameter("format");
        if (type == null)   type = "membres";
        if (format == null) format = "pdf";

        String titre;
        String[] entetes;
        List<String[]> lignes = new ArrayList<>();

        switch (type) {
            case "cotisations" -> {
                titre = "Liste des cotisations";
                entetes = new String[]{"Membre", "Numéro", "Période", "Montant (FCFA)",
                        "Date paiement", "Mode", "Statut"};
                for (Cotisation c : cotisationService.trouverToutes()) {
                    lignes.add(new String[]{
                            c.getMembre().getNomComplet(),
                            c.getMembre().getNumero(),
                            c.getNomMois() + " " + c.getAnnee(),
                            c.getMontant().toPlainString(),
                            String.valueOf(c.getDatePaiement()),
                            c.getModePaiement(),
                            String.valueOf(c.getStatut())
                    });
                }
            }
            case "amendes" -> {
                titre = "Liste des amendes";
                entetes = new String[]{"Membre", "Numéro", "Montant (FCFA)",
                        "Date génération", "Statut"};
                for (Amende a : amendeService.trouverToutes()) {
                    lignes.add(new String[]{
                            a.getMembre().getNomComplet(),
                            a.getMembre().getNumero(),
                            a.getMontant().toPlainString(),
                            String.valueOf(a.getDateGeneration()),
                            String.valueOf(a.getStatutPaiement())
                    });
                }
            }
            default -> {
                titre = "Liste des membres";
                entetes = new String[]{"Numéro", "Prénom", "Nom", "Email",
                        "Date adhésion", "Rôle", "Statut"};
                for (Membre m : membreService.trouverTous()) {
                    lignes.add(new String[]{
                            m.getNumero(),
                            m.getPrenom(),
                            m.getNom(),
                            m.getEmail(),
                            String.valueOf(m.getDateAdhesion()),
                            String.valueOf(m.getRole()),
                            String.valueOf(m.getStatut())
                    });
                }
            }
        }

        String nomFichier = type + "_" + LocalDate.now();

        if ("excel".equalsIgnoreCase(format)) {
            ecrireExcel(resp, nomFichier, titre, entetes, lignes);
        } else {
            ecrirePdf(resp, nomFichier, titre, entetes, lignes);
        }
    }

    // ---------- Excel (.xlsx via Apache POI) ----------
    private void ecrireExcel(HttpServletResponse resp, String nomFichier, String titre,
                             String[] entetes, List<String[]> lignes) throws IOException {

        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + nomFichier + ".xlsx\"");

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet(titre);

            // Style en-tête : gras + fond bleu
            org.apache.poi.ss.usermodel.Font fontEntete = wb.createFont();
            fontEntete.setBold(true);
            fontEntete.setColor(IndexedColors.WHITE.getIndex());
            CellStyle styleEntete = wb.createCellStyle();
            styleEntete.setFont(fontEntete);
            styleEntete.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            styleEntete.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row ligneEntete = sheet.createRow(0);
            for (int i = 0; i < entetes.length; i++) {
                Cell cell = ligneEntete.createCell(i);
                cell.setCellValue(entetes[i]);
                cell.setCellStyle(styleEntete);
            }

            int r = 1;
            for (String[] ligne : lignes) {
                Row row = sheet.createRow(r++);
                for (int i = 0; i < ligne.length; i++) {
                    row.createCell(i).setCellValue(ligne[i] == null ? "" : ligne[i]);
                }
            }

            for (int i = 0; i < entetes.length; i++) {
                sheet.autoSizeColumn(i);
            }

            OutputStream out = resp.getOutputStream();
            wb.write(out);
            out.flush();
        }
    }

    // ---------- PDF (via OpenPDF) ----------
    private void ecrirePdf(HttpServletResponse resp, String nomFichier, String titre,
                           String[] entetes, List<String[]> lignes) throws IOException {

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + nomFichier + ".pdf\"");

        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 42, 36);
        try {
            PdfWriter.getInstance(doc, resp.getOutputStream());
            doc.open();

            Font fontTitre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(13, 110, 253));
            Paragraph titreP = new Paragraph(titre, fontTitre);
            titreP.setSpacingAfter(4);
            doc.add(titreP);

            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
            Paragraph sub = new Paragraph(
                    "Association UCAD — Gestion des cotisations  |  Édité le " + LocalDate.now(), fontSub);
            sub.setSpacingAfter(12);
            doc.add(sub);

            PdfPTable table = new PdfPTable(entetes.length);
            table.setWidthPercentage(100);

            Font fontEntete = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
            for (String entete : entetes) {
                PdfPCell cell = new PdfPCell(new Phrase(entete, fontEntete));
                cell.setBackgroundColor(new Color(13, 110, 253));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            Font fontCellule = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
            boolean alterne = false;
            for (String[] ligne : lignes) {
                for (String valeur : ligne) {
                    PdfPCell cell = new PdfPCell(new Phrase(valeur == null ? "" : valeur, fontCellule));
                    cell.setPadding(4);
                    if (alterne) cell.setBackgroundColor(new Color(243, 246, 252));
                    table.addCell(cell);
                }
                alterne = !alterne;
            }

            if (lignes.isEmpty()) {
                PdfPCell vide = new PdfPCell(new Phrase("Aucune donnée", fontCellule));
                vide.setColspan(entetes.length);
                vide.setHorizontalAlignment(Element.ALIGN_CENTER);
                vide.setPadding(10);
                table.addCell(vide);
            }

            doc.add(table);
        } catch (Exception e) {
            throw new IOException("Erreur lors de la génération du PDF", e);
        } finally {
            if (doc.isOpen()) doc.close();
        }
    }
}
