package com.analysys.automation.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class PoiUtils {
    public static Iterator<Sheet> readExcel(String fileName) throws Exception {
        InputStream is = new FileInputStream(new File(fileName));

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

        return xssfWorkbook.sheetIterator();
    }

    public static List<ArrayList<Object>> readSheet(Sheet sheet, int beginColumnNum, int endColumnNum) {
        List<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();

        //读取标题
        Row title = sheet.getRow(0);
        //读取所有列，但不应该包括第一列（标题列）
        sheet.removeRow(title);
        Iterator<Row> rows = sheet.rowIterator();

        int iRow = 0;
        while (rows.hasNext()) {
            Row row = rows.next();
            ArrayList<Object> rowList = new ArrayList<Object>();
            Iterator<Cell> cells = row.cellIterator();
            int iCell = 0;
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (iCell < beginColumnNum) {
                    iCell++;
                    continue;
                }

                if (iCell > endColumnNum) {
                    break;
                }

                if (cell.getCellType() == CellType.BLANK) {
                    break;
                }
                //遇到空值，直接返回已经保存的list信息，不再读取
                if (StringUtils.isBlank(cell.getStringCellValue())) {
                    return list;
                }
                rowList.add(cell.getStringCellValue().trim());
                iRow++;
            }
            list.add(rowList);
        }
        return list;
    }

    public static List<List<Row>> readSheet(Sheet sheet) {
        List<List<Row>> list = new ArrayList<List<Row>>();

        //读取标题
        Row title = sheet.getRow(0);
        //读取所有列，但不应该包括第一列（标题列）
        sheet.removeRow(title);
        Iterator<Row> rows = sheet.rowIterator();

        while (rows.hasNext()) {
            Row row = rows.next();
            List<Row> rowList = new ArrayList<Row>();

            list.add(rowList);
        }
        return list;
    }

    public static String writeExcel(String filePath, String fileName, String sheetName, List<ArrayList<Object>> rows) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();

        Sheet sheet = wb.createSheet(sheetName);

        File file = new File(filePath);
        if (!file.exists())
            file.mkdirs();

        FileOutputStream fos = new FileOutputStream(filePath + fileName);
        writeRows(sheet, rows);
        wb.write(fos);
        fos.close();

        return filePath + fileName;
    }

    private static Sheet writeRows(Sheet sheet, List<ArrayList<Object>> rows) {
        for(int i = 0; i<rows.size(); i++) {
            sheet = writeOneRow(sheet, rows.get(i), i);
        }
        return sheet;
    }

    private static Sheet writeOneRow(Sheet sheet, List<Object> titleList, int index) {
        Row tempRow = sheet.createRow(index);
        for(int i=0; i<titleList.size(); i++) {
            Cell cell = tempRow.createCell(i);
            Object obj = titleList.get(i);
            if(obj == null) {
                cell.setCellValue("");
            }else if(obj instanceof String) {
                cell.setCellValue(obj.toString());
            } else if(obj instanceof Double) {
                cell.setCellValue(Double.parseDouble(obj.toString()));
            } else if(obj instanceof Integer) {
                cell.setCellValue(Integer.parseInt(obj.toString()));
            } else
                cell.setCellValue(obj.toString());
        }
        return sheet;
    }

    public static Object readCellValue(XSSFCell cell) {
        Object cellValue = new Object();
        CellType cellType = cell.getCellType();
        if (cell != null) {
            switch (cellType) {
                case BLANK:
                    cellValue = "";
                    break;
                case BOOLEAN:
                    cellValue = cell.getBooleanCellValue();
                    break;
                case ERROR:
                    cellValue = "Bad value!";
                    break;
                case NUMERIC:
                    cellValue = cell.getNumericCellValue();
                    break;
                case FORMULA:
                    try {
                        cellValue = cell.getNumericCellValue();
                    } catch (IllegalStateException e) {
                        try {
                            cellValue = cell.getRichStringCellValue().toString();
                        } catch (IllegalStateException e2) {
                            cellValue = cell.getErrorCellString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    cellValue = cell.getRichStringCellValue().getString();
            }
        }
        else
            cellValue = "";

        return cellValue;
    }

    public static void main(String[] args) throws Exception {
//        Iterator<Sheet> it = readExcel("E:\\develop\\dataapi接口.xlsx");
//        while (it.hasNext()) {
//            XSSFSheet sh = (XSSFSheet) it.next();
//            System.out.println(sh.getSheetName());
//            readSheet(sh, 0, 10).forEach(list -> {
//                list.stream().forEach(value -> {
//                    System.out.print(value + "  ");
//                });
//                System.out.println();
//            });
//        }
        System.out.println(UUID.randomUUID().toString().toUpperCase());

        for(int i=0; i<30; i++) {
            System.out.println((int) (Math.random() * 10) % 2);
        }
    }
}
