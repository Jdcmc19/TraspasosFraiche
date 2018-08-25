package Classes;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

public class Excel {
    public static ArrayList<Traspaso> leerExcel(File excelFile, int traspaso){
        InputStream excelStream = null;
        Traspaso trasp;
        ArrayList<Traspaso> total = new ArrayList<>();
        try {
            excelStream = new FileInputStream(excelFile);
            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(excelStream);
            XSSFSheet hssfSheet = hssfWorkbook.getSheet("TRASPASOS");
            XSSFRow hssfRow;
            XSSFCell cell;
            if(hssfSheet==null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ocurrió un error");
                alert.setHeaderText("Archivo no correspondiente.");
                alert.setContentText("Verifique que el archivo sea correcto y la hoja se llame \"TRASPASOS\"");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        alert.close();
                    }
                });
            }
            int rows = hssfSheet.getLastRowNum();
            int cols = 0;
            for (int r = 1; r < rows; r++) {
                hssfRow = hssfSheet.getRow(r);
                if (hssfRow == null){
                    break;
                }else{
                    XSSFCell cellArticulo = hssfRow.getCell(0);
                    String articulo = cellArticulo== null?"":
                            (cellArticulo.getCellType() == Cell.CELL_TYPE_STRING)?cellArticulo.getStringCellValue():
                                    (cellArticulo.getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + cellArticulo.getNumericCellValue():
                                            (cellArticulo.getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + cellArticulo.getBooleanCellValue():
                                                    (cellArticulo.getCellType() == Cell.CELL_TYPE_BLANK)?"BLANK":
                                                            (cellArticulo.getCellType() == Cell.CELL_TYPE_FORMULA)?"" + cellArticulo.getNumericCellValue():
                                                                    (cellArticulo.getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                    XSSFCell cellDescripcion = hssfRow.getCell(1);
                    String descripcion = cellDescripcion== null?"":
                            (cellDescripcion.getCellType() == Cell.CELL_TYPE_STRING)?cellDescripcion.getStringCellValue():
                                    (cellDescripcion.getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + cellDescripcion.getNumericCellValue():
                                            (cellDescripcion.getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + cellDescripcion.getBooleanCellValue():
                                                    (cellDescripcion.getCellType() == Cell.CELL_TYPE_BLANK)?"BLANK":
                                                            (cellDescripcion.getCellType() == Cell.CELL_TYPE_FORMULA)?"" + cellDescripcion.getNumericCellValue():
                                                                    (cellDescripcion.getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                    XSSFCell cellValue = hssfRow.getCell(traspaso);
                    String value = cellValue== null?"":
                            (cellValue.getCellType() == Cell.CELL_TYPE_STRING)?cellValue.getStringCellValue():
                                    (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + cellValue.getNumericCellValue():
                                            (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + cellValue.getBooleanCellValue():
                                                    (cellValue.getCellType() == Cell.CELL_TYPE_BLANK)?"BLANK":
                                                            (cellValue.getCellType() == Cell.CELL_TYPE_FORMULA)?"" + cellValue.getNumericCellValue():
                                                                    (cellValue.getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                    if(!value.equals("0.0") && !articulo.equals("")){
                        trasp = new Traspaso(articulo,descripcion,value);
                        total.add(trasp);
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("The file not exists (No se encontró el fichero): " + fileNotFoundException);
        } catch (IOException ex) {
            System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
                System.out.println("Error in file processing after close it (Error al procesar el fichero después de cerrarlo): " + ex);
            }
        }
        return total;
    }
    public static Workbook escribirExcel(String traspaso, ArrayList<Traspaso> array){
        String[] columns = {"ARTICULO", "DESCRIPCION", traspaso};

        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet(traspaso);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.GREEN.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for(Traspaso trasp: array) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(trasp.getArticulo());

            row.createCell(1)
                    .setCellValue(trasp.getDescripcion());

            row.createCell(2)
                    .setCellValue(trasp.getValue());
        }
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;

    }
}
