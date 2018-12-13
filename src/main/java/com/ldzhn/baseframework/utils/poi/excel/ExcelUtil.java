package com.ldzhn.baseframework.utils.poi.excel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ldzhn.baseframework.utils.FileUtil;
import com.ldzhn.baseframework.utils.poi.bean.CellField;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * @Author : llliu
 * @Despriction : Excel操作工具类
 *  支持功能：
 *
 * @Date : create on 2018/9/20 10:21
 */
public final class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    /** 私有无参构造方法 **/
    private ExcelUtil() { }

    /**
     * 生成excel2007文档
     *
     * @param filePath 文件保存路径 ，例如：D:/excel/test.xlsx
     * @param beans 实体对象
     */
    public static <T> void createExcel(String filePath, List<T> beans) {
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        Workbook wb = createWorkbook(beans, fileName);
        File file = FileUtil.createFile(filePath);
        try (OutputStream os = new FileOutputStream(file)) {
            wb.write(os);
        } catch (IOException e) {
            LOGGER.error("生成excel2007文件失败", e);
        }
    }

    /**
     * 导出excel2007文件
     *
     * @param response 响应对象
     * @param beans 实体对象集合
     * @param <T> 泛型类
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> beans) {
        // 以当前时间作为文件名
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 创建Workbook对象
        Workbook wb = createWorkbook(beans, fileName);
        // 导出excel
        try (OutputStream os = response.getOutputStream()) {
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setContentType("application/msexcel");
            wb.write(os);
        } catch (IOException e) {
            LOGGER.error("导出excel2007文件失败", e);
        }
    }

    /**
     * 解析Excel文档，转成对象集合返回
     *
     * @param filePath 文件存放路径，例如：D:/excel/test.xlsx
     * @param clazz 泛型类类型
     * @return List<T> 对象集合
     */
    public static <T> List<T> parseExcel(String filePath, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        InputStream input = null;
        try {
            input = new FileInputStream(new File(filePath));
            if (input != null && !input.markSupported()) {
                input = new PushbackInputStream(input, 8);
            }
            Workbook wb = getWorkbook(input);
            if (wb != null) {
                list = getBeanList(wb, clazz);
            }
        } catch (Exception e) {
            LOGGER.error("解析文件失败", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("关闭流失败", e);
                }
            }
        }
        return list;
    }

    /**
     * 解析Excel文档，转成对象集合返回
     *
     * @param input 文件输入流对象
     * @param clazz 类类型
     * @param <T> 泛型类型
     * @return List<T>
     */
    public static <T> List<T> parseExcel(InputStream input, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            if (!input.markSupported()) {
                input = new PushbackInputStream(input, 8);
            }
            Workbook wb = getWorkbook(input);
            if (wb != null) {
                list = getBeanList(wb, clazz);
            }
        } catch (Exception e) {
            LOGGER.error("解析文件失败", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("关闭流失败", e);
                }
            }
        }
        return list;
    }

    /**
     * 删除该目录下所有文件
     * @param filePath 文件目录路径，如：d:/test/
     * @return boolean
     */
    public static boolean deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile() && f.delete()) {
                        LOGGER.info("删除" + f.getName() + "文件成功");
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 创建Workbook对象
     *
     * @param beans 实体集合对象
     * @param fileName 文件名
     * @return Workbook
     */
    private static <T> Workbook createWorkbook(List<T> beans, String fileName) {
        // 1.创建2007版工作簿
        Workbook wb = new XSSFWorkbook();
        // 2.创建工作表
        Sheet sheet = wb.createSheet(fileName);
        // 3.获取实体类标有@CellField注解的Field对象
        List<Field> fields = getFields(beans);
        // 4.设置列宽
        for (int i = 0; i < fields.size(); i++) {
            sheet.setColumnWidth((short) i, (short) (20.7 * 150));
        }
        // 5.写入标题
        Row row = sheet.createRow(0);
        writeTitles(fields, row);
        // 6.写入内容
        writeContents(beans, fields, sheet, row);
        return wb;
    }

    /**
     * 获取Workbook对象
     *
     * @param input 输入流
     * @return Workbook
     */
    private static Workbook getWorkbook(InputStream input) {
        try {
            if (POIFSFileSystem.hasPOIFSHeader(input)) {
                return new HSSFWorkbook(input); // 得到2003工作簿
            }
            if (DocumentFactoryHelper.hasOOXMLHeader(input)) {
                try (OPCPackage op = OPCPackage.open(input)) {
                    return new XSSFWorkbook(op); // 得到2007工作簿
                } catch (Exception e) {
                    LOGGER.error("获取Workbook对象失败", e);
                }
            }
        } catch (IOException e) {
            LOGGER.error("获取Workbook对象失败", e);
        }
        return null;
    }



    /**
     * 写入标题
     *
     * @param fields Field对象集合
     * @param row 行对象
     */
    private static void writeTitles(List<Field> fields, Row row) {
        if (fields != null && !fields.isEmpty()) {
            for (int i = 0; i < fields.size(); i++) {
                CellField cellField = fields.get(i).getAnnotation(CellField.class);
                if (cellField == null) {
                    continue;
                }
                Cell cell = row.createCell(i);
                // 获取带有name属性的值并写入
                if (StringUtils.isNotBlank(cellField.name())) {
                    cell.setCellValue(cellField.name());
                }
            }
        }
    }

    /**
     * 写入内容
     *
     * @param beans 实体对象集合
     * @param fields Field对象集合
     * @param sheet 工作表对象
     * @param row 行对象
     * @param <T> 泛型类
     */
    private static <T> void writeContents(List<T> beans, List<Field> fields, Sheet sheet, Row row) {
        for (int i = 0; i < beans.size(); i++) {
            T t = beans.get(i);
            row = sheet.createRow(i + 1);
            for (int j = 0; j < fields.size(); j++) {
                CellField cellField = fields.get(j).getAnnotation(CellField.class);
                if (cellField != null) {
                    Class<?> valType = fields.get(j).getType(); // 获取属性类型
                    String fieldName = fields.get(j).getName(); // 获取属性名
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = ReflectionUtils.findMethod(t.getClass(), methodName);
                    setValues(method, t, j, valType, cellField, row); // 设置值
                }
            }
        }
    }

    /**
     * 设置值
     *
     * @param method 方法对象
     * @param t 泛型对象
     * @param j 下标
     * @param valType 类型对象
     * @param cellField 注解对象
     * @param row 行对象
     * @param <T> 泛型
     */
    private static <T> void setValues(Method method, T t, int j, Class<?> valType, CellField cellField, Row row) {
        if (method != null) {
            Object value = ReflectionUtils.invokeMethod(method, t);
            if (value == null) {
                row.createCell(j).setCellValue("");
            } else if (valType == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (StringUtils.isNotBlank(cellField.name())) { // 默认日期类型格式：yyyy-MM-dd
                    row.createCell(j).setCellValue(sdf.format((Date) value));
                }
            } else if (valType == Double.class) {  // 小数类型
                row.createCell(j).setCellValue(Double.parseDouble(value.toString()));
            } else if (valType == BigDecimal.class) {  // BigDecimal类型
                row.createCell(j).setCellValue(new BigDecimal(value.toString()).doubleValue());
            } else if (valType == Integer.class) {  // 整数型
                row.createCell(j).setCellValue(Integer.parseInt(value.toString()));
            } else { // 字符串
                row.createCell(j).setCellValue(value.toString());
            }
        }
    }

    /**
     * 通过泛型转换为对象集合
     *
     * @param wb 工作簿对象
     * @param clazz 实体对象类类型
     * @param <T> 泛型类型
     * @return <T> 泛型实体对象
     */
    private static <T> List<T> getBeanList(Workbook wb, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        // 注解名称与字段属性的对应关系
        Map<String, Field> annoMap = getFields(clazz);
        // 列所引与标题名称对应关系
        Map<Integer, String> titleMap = new HashMap<Integer, String>();
        Sheet sheet = wb.getSheetAt(0); // 获取第一张工作表
        Iterator<Row> rows = sheet.iterator(); // 利用迭代器，取出每一个行
        int index = 0;
        while (rows.hasNext()) {
            Row row = rows.next(); // 每一行
            Iterator<Cell> cells = row.iterator(); // 利用迭代器，取出每一个格
            if (index == 0) {
                // 获取第一行标题
                while (cells.hasNext()) {
                    Cell cell = cells.next(); // 每一格
                    titleMap.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
                }
            } else {
                // 创建实体对象，把值设置进去
                if (!annoMap.isEmpty() && !titleMap.isEmpty()) {
                    list.add(buildBean(annoMap, titleMap, clazz, cells));
                }
            }
            index++;
        }
        return list;
    }

    /**
     * 创建实体，设置值
     *
     * @param annoMap 注解名称与字段属性的对应的Map
     * @param titleMap excel列所引与标题名称对应的Map
     * @param clazz 实体对象类类型
     * @param cells 每一行的所有格子
     * @return List<T>
     */
    private static <T> T buildBean(Map<String, Field> annoMap, Map<Integer, String> titleMap, Class<T> clazz, Iterator<Cell> cells) {
        T t = null;
        try {
            t = clazz.newInstance();
            while (cells.hasNext()) {
                Cell cell = cells.next(); // 每一格
                String title = titleMap.get(cell.getColumnIndex());
                if (annoMap.containsKey(title)) {
                    Field field = annoMap.get(title);
                    Class<?> valType = field.getType();
                    Object value = getType(cell.getStringCellValue(), valType);
                    String fieldName = field.getName();
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = ReflectionUtils.findMethod(clazz, methodName, valType);
                    if (method != null) {
                        ReflectionUtils.invokeMethod(method, t, value);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("创建泛型实体对象失败", e);
        }
        return t;
    }

    /**
     * 注解名称与字段属性的对应关系
     *
     * @param clazz 实体对象类类型
     * @return Map<String,Field>
     */
    private static <T> Map<String, Field> getFields(Class<T> clazz) {
        Map<String, Field> annoMap = new HashMap<String, Field>();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field filed : fileds) {
            CellField cellField = filed.getAnnotation(CellField.class);
            if (cellField != null && StringUtils.isNotBlank(cellField.name())) {
                annoMap.put(cellField.name(), filed);
            }
        }
        return annoMap;
    }

    /**
     * 获取标有@CellField注解的Field对象
     *
     * @param beans 对象集合
     * @return List<Field>
     */
    private static <T> List<Field> getFields(List<T> beans) {
        Class<? extends Object> cls = beans.get(0).getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        List<Field> annoFields = new ArrayList<Field>();
        // 筛选出标有注解的字段
        for (Field field : declaredFields) {
            CellField anno = field.getAnnotation(CellField.class);
            if (anno != null) {
                annoFields.add(field);
            }
        }
        return annoFields;
    }

    /**
     * 转换成实体属性对应的类型
     *
     * @param value 每一格的数值
     * @param valType 实体属性类型
     * @return Object 转换为对应类型以obj返回
     */
    private static <T> Object getType(String value, Class<T> valType) {
        try {
            if (valType == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(value);
            } else if (valType == Double.class) {
                return Double.parseDouble(value);
            } else if (valType == BigDecimal.class) {
                return new BigDecimal(value);
            } else if (valType == Integer.class) {
                return Integer.parseInt(value);
            } else if (valType == Long.class) {
                return Long.parseLong(value);
            } else if (valType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            LOGGER.error("类型转换异常", e);
        }
        return value;
    }

    /**
     * 根据后缀判断是否为 Excel 文件，后缀匹配xls和xlsx
     *
     * @param pathname
     * @return
     */
    public static boolean isExcel(String pathname) {
        if (pathname == null) {
            return false;
        }
        return pathname.endsWith(".xls") || pathname.endsWith(".xlsx");
    }


}
