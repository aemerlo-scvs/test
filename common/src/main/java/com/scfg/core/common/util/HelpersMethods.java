package com.scfg.core.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.exception.OperationException;
import com.sun.org.apache.xpath.internal.objects.XString;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.scfg.core.common.util.HelpersConstants.*;



public class HelpersMethods {

    public static boolean enableVerifyGenericClass = false;
    public static Class<?> paramVerifyClass = Integer.class;

    public static Long c = 0l;


    public static final Map<String, Class> BUILT_IN_MAP =
            new ConcurrentHashMap<String, Class>();

    static {
        c++;
        for (Class c : new Class[]{void.class, boolean.class, byte.class, char.class,
                short.class, int.class, float.class, double.class, long.class,
                Boolean.class, Byte.class, Character.class,
                Short.class, Integer.class, Float.class, Double.class, Long.class,
                BigInteger.class, String.class, LocalDate.class, Date.class})
            BUILT_IN_MAP.put(c.getName(), c);
    }

    public static Class getClassForName(String name) throws ClassNotFoundException {
        Class c = BUILT_IN_MAP.get(name);
        if (isNull(c)) {
            // assumes you have only one class loader!
            BUILT_IN_MAP.put(name, c = Class.forName(name));
        }
        return c;
    }


    public static boolean verifyPrimitiveClassForName(String name) {
        Class c = BUILT_IN_MAP.get(name);
        return !isNull(c);
        /*if (c == null)
            // assumes you have only one class loader!
            BUILT_IN_MAP.put(name, c = Class.forName(name));
        return c;*/
    }

    public static <T> List<T> castObjectsToDTO(List<Object> objects) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return objectMapper.convertValue(objects, new TypeReference<List<T>>() {
        });
    }

    public static ObjectMapper mapper() {

        return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        /*return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);*/
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] p = param.split("=");
            String name = p[0];
            if (p.length > 1) {
                String value = p[1];
                map.put(name, value);
            }
        }
        return map;
    }

    public static boolean set(Object objectClass, String fieldName, Object fieldValue) {
        Class<?> clazz = objectClass.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(objectClass, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    /*
    *
    * Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
        set(instance, "salary", 15);
        set(instance, "firstname", "John");
    *
    * */

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> get(Object objectClass, String fieldName) {
        Class<?> clazz = objectClass.getClass();
        boolean fieldFound = false;
        Map<String, T> values = new HashMap<>();
        while (!isNull(clazz) && !fieldFound) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                values.put("metadata", (T) field);
                values.put("fieldExists", (T) Boolean.TRUE);
                values.put("data", (T) field.get(objectClass));
                fieldFound = true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                values.put("metadata", null);
                values.put("fieldExists", (T) Boolean.FALSE);
                values.put("data", null);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return values;
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public static <T, U> void mergeValues(Object objBaseClass, Object objMergeClass) throws NoSuchFieldException, IllegalAccessException {
        if (!isNull(objBaseClass)) {
            Class<T> baseClass = (Class<T>) objBaseClass.getClass();
            Class<U> mergeClass = (Class<U>) objMergeClass.getClass();

            List<Field> mergeClassFields = getAllFields(mergeClass); //mergeClass.getDeclaredFields();
            //List<Field> baseClassFields = Arrays.asList(baseClass.getDeclaredFields());
            try {
                for (Field mergeClassField : mergeClassFields) {
                    String mergeFieldName = mergeClassField.getName();
                    Map<String, Object> basePropField = get(objBaseClass, mergeFieldName);
                    Map<String, Object> mergePropField = get(objMergeClass, mergeFieldName);
                    //Map<String, Object> mergeProp = get(baseClass,mergeFieldName);
                    boolean baseFieldSearch = (Boolean) basePropField.get("fieldExists");
                    if (baseFieldSearch) { // field A found B
                        Object baseValue = basePropField.get("data");
                        Object mergeValue = mergePropField.get("data");


                        Field baseField = (Field) basePropField.get("metadata");
                        //baseField = baseField.getClass();
                        if (!isNull(mergeValue)) { // assign value B in A
                            Class<?> mergeType = mergeClassField.getType();
                            String mergeClassName = mergeType.getName();

                            Class<?> baseType = baseField.getType();
                            String baseClassName = baseType.getName();


                            boolean mergeIsPrimitive = verifyPrimitiveClassForName(mergeClassName);
                            if (isNull(baseValue) && !mergeIsPrimitive) {
                                baseValue = Class.forName(baseClassName)
                                        .getDeclaredConstructor().newInstance();
                            }
                            if (mergeIsPrimitive) {
                                set(objBaseClass, mergeFieldName, mergeValue);
                            } else {
                                mergeValues(baseValue, mergeValue);
                                set(objBaseClass, mergeFieldName, baseValue);
                            }

                        /*if (type.equals(BaseJpaEntity.class)){
                            mergeValue(basePropField, sa)
                        }*/
                            //set(objBaseClass, mergeFieldName, mergeValue);
                        }
                    }
                }
            /*if (((String)value).equalsIgnoreCase("true") || ((String)value).equalsIgnoreCase("false"))

            try {
                value = Boolean.parseBoolean((String)value);
            } catch ()*/
            /*Object value = mergeClassField.get(objBaseClass);
            if (!isNull(value)){
                int index = baseClassFields.indexOf(mergeClassField);
                Field baseClassField = baseClassFields.get(index);
                baseClassField.setAccessible(true);
                bas
            }*/
            } catch (Exception e) {
                String err = e.getMessage();
            }
        }
    }
    /*
    * Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
        int salary = get(instance, "salary");
        String firstname = get(instance, "firstname");
    * */

    public static <T> boolean isNull(T object) {
        return object == null;
    }


    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static String saveImageToPath(String path, String filename, String mimetype, BufferedImage image) {

        //write image
        File pathAsFile = new File(path);
        String ext = "";
        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdirs();

        }
        switch (mimetype) {
            case JPEG:
                path = path + filename + ".jpeg";
                ext = "jpeg";
                break;
            case JPG:
                path = path + filename + ".jpg";
                ext = "jpg";
                break;
            case PNG:
                path = path + filename + ".png";
                ext = "png";
                break;
            default:
                break;

        }

        File f = null;
        try {
            f = new File(path);
            ImageIO.write(image, ext, f);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return path;
    }

    public static String savePdfToPath(String path, String fileName, String content) {

        File pathAsFile = new File(path);
        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdirs();
        }
        path = path + fileName + ".pdf";
        byte[] decoded = java.util.Base64.getDecoder().decode(content);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            BufferedOutputStream bout=new BufferedOutputStream(fos);
            try {
                bout.write(decoded);
                //fos.write(decoded);
                bout.flush();
                bout.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static BufferedImage resize(BufferedImage bufferedImage, int newW, int newH) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage imageRedirection = new BufferedImage(newW, newH, bufferedImage.getType());
        Graphics2D g = imageRedirection.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bufferedImage, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return imageRedirection;

    }

    public static String generateCodeWithCapitalLettersAndNumbers(int numberOfDigits) {

        String code = "";

        for (int i = 1; i <= numberOfDigits; i++) {
            int maxRange = 2;
            switch ((int) Math.floor((Math.random() * maxRange) + 1)) {
                case 1:
                    code += getRandomCapitalLetterWithAForbiddenChar('O');
                    break;
                case 2:
                    code += getRandomNumber();
                    break;
            }
        }
        return code;
    }

    public static byte[] base64Byte(String content) {
        if (content != null || !content.isEmpty()) {
            Base64.Decoder decoder = Base64.getUrlDecoder();
            byte[] bytes = decoder.decode(content);
            return bytes;
        }
        return null;
    }

    public static char getRandomCapitalLetterWithAForbiddenChar(char forbiddenChar) {
        int minRange = 65;
        int maxRange = 90;
        char resultChar = (char) Math.floor(Math.random() * (maxRange - minRange + 1) + minRange);
        if (resultChar == forbiddenChar) {
            return getRandomCapitalLetterWithAForbiddenChar(forbiddenChar);
        }
        return resultChar;
    }

    public static char getRandomCapitalLetter() {
        int minRange = 65;
        int maxRange = 90;
        return (char) Math.floor(Math.random() * (maxRange - minRange + 1) + minRange);
    }

    public static char getRandomLowerCaseLetter() {
        int minRange = 97;
        int maxRange = 122;
        return (char) Math.floor(Math.random() * (maxRange - minRange + 1) + minRange);
    }

    public static int getRandomNumber() {
        int maxRange = 9;
        return (int) Math.floor(Math.random() * (maxRange + 1));
    }

    public static String convertNumberToLiteral(String number, boolean inCapitalLetters) {
        String literal = "";
        String parte_decimal;
        String moneyDescription = "Bolivianos.";

        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        number = number.replace(".", ",");
        //si el numero no tiene parte decimal, se le agrega ,00
        if (!number.contains(",")) {
            number = number + ",00";
        }

        //se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,9},\\d{1,2}", number)) {
            //se divide el numero 0000000,00 -> entero y decimal
            String Num[] = number.split(",");
            //de da formato al numero decimal
            parte_decimal = "y " + Num[1] + "/100 " + moneyDescription;
            //se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
                literal = getMillions(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) { //si es miles
                literal = getThousands(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) { //si es centena
                literal = getHundreds(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) { //si es decena
                literal = getTens(Num[0]);
            } else { //sino unidades -> 9
                literal = getUnits(Num[0]);
            }
            //devuelve el resultado en mayusculas o minusculas
            if (inCapitalLetters) {
                return (literal + parte_decimal).toUpperCase();
            } else {
                return (literal + parte_decimal);
            }
        } else { //error, no se puede convertir
            return literal = null;
        }
    }

    public static String convertNumberToCompanyFormatNumber(Double convertNumber) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        sym.setGroupingSeparator('.');
        format.setDecimalFormatSymbols(sym);
        return format.format(convertNumber);
    }

    public static String convertNumberToCompanyFormatNumberAndCurrency(Double convertNumber, String currencyType) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        sym.setGroupingSeparator('.');
        format.setDecimalFormatSymbols(sym);
        String formatting = currencyType + format.format(convertNumber);
        return formatting;
    }

    //#region Funciones para convertir los numeros a literales

    private static String getUnits(String number) { // 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009 = 9
        String num = number.substring(number.length() - 1);
        return HelpersConstants.UNITS[Integer.parseInt(num)];
    }

    private static String getTens(String num) {// 99
        int n = Integer.parseInt(num);
        if (n < 10) { //para 01 - 09
            return getUnits(num);
        } else if (n > 19) { //para 20 - 99
            String u = getUnits(num);
            if (u.equals("")) { //para 20,30,40,50,60,70,80,90
                return HelpersConstants.TENS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return HelpersConstants.TENS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            }
        } else { //números entre 11 y 19
            return HelpersConstants.TENS[n - 10];
        }
    }

    private static String getHundreds(String num) { // 999 o 099
        if (Integer.parseInt(num) > 99) { //es centena
            if (Integer.parseInt(num) == 100) { //caso especial
                return " cien ";
            } else {
                return HelpersConstants.HUNDREDS[Integer.parseInt(num.substring(0, 1))] + getTens(num.substring(1));
            }
        } else { //por Ej. 099
            //se quita el 0 antes de convertir a decenas
            return getTens(Integer.parseInt(num) + "");
        }
    }

    private static String getThousands(String number) { //999 999
        //obtiene las centenas
        String c = number.substring(number.length() - 3);
        //obtiene los miles
        String m = number.substring(0, number.length() - 3);
        String n = "";
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getHundreds(m);
            return n + "mil " + getHundreds(c);
        } else {
            return "" + getHundreds(c);
        }

    }

    private static String getMillions(String number) { //000 000 000
        //se obtiene los miles
        String thousands = number.substring(number.length() - 6);
        //se obtiene los millones
        String millions = number.substring(0, number.length() - 6);
        String n = "";

//        if (millon.length() > 1) {
//            n = getHundreds(millon) + "millones ";
//        } else {
//            n = getUnits(millon) + "millon ";
//        }

        if (Integer.parseInt(millions) == 1) {
            n = getHundreds(millions) + "millon ";
        } else {
            n = getUnits(millions) + "millones ";
        }

        return n + getThousands(thousands);
    }

    //#endregion


    public static <T> T getValueObject(T object) {
        if (!isNull(object)) {
            boolean isLocalDate = object.getClass().isAssignableFrom(LocalDate.class);
            if (isLocalDate) {
                LocalDate value = (LocalDate) object;
                object = (T) value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        }
        return object;
    }

    public static boolean equalsClassifiers(Long referenceCodeSrc, Long refereceTypeCodeSrc, ClassifierEnum classifierEnum) {
        long referenceCodeDest = classifierEnum.getReferenceCode();
        ;
        long referenceTypeCodeDest = classifierEnum.getReferenceCodeType();
        ;
        return referenceCodeSrc.compareTo(referenceCodeDest) == 0
                && refereceTypeCodeSrc.compareTo(referenceTypeCodeDest) == 0;
    }


    public static Map<String, Integer> forwardPeriod(int numberMonth, int year) {
        Map<String, Integer> nextPeriod = new HashMap<>();
        int nextMonthValue = (numberMonth + 1) % LAST_NUMBER_MONTH;
        int nextYear = year;
        nextPeriod.put(KEY_MONTH, nextMonthValue);
        if (numberMonth == LAST_NUMBER_MONTH) {
            nextYear++;
        }
        nextPeriod.put(KEY_YEAR, nextYear);
        return nextPeriod;
    }

    public static Map<String, Integer> backwardPeriod(int numberMonth, int year) {
        Map<String, Integer> nextPeriod = new HashMap<>();
        int nextMonthValue = numberMonth == FIRST_NUMBER_MONTH ? LAST_NUMBER_MONTH : numberMonth - 1;
        int nextYear = year;
        nextPeriod.put(KEY_MONTH, nextMonthValue);
        if (numberMonth == FIRST_NUMBER_MONTH) {
            nextYear--;
        }
        nextPeriod.put(KEY_YEAR, nextYear);
        return nextPeriod;
    }

    public static Map<String, Integer> getPeriod(int numberMonth, int year, boolean orderAsc) {
        return orderAsc ? forwardPeriod(numberMonth, year) : backwardPeriod(numberMonth, year);

    }

    public static String formatStringDate(Date date) {
        return new SimpleDateFormat("ddMMyyyyHHmmss").format(date).toString();
    }

    public static String formatStringOnlyDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
    }

    public static String formatStringOnlyHourAndMinute(Date date) {
        return new SimpleDateFormat("HH:mm").format(date).toString();
    }

    public static String formatStringOnlyDateAndHour(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date).toString();
    }

    public static Date formatOfStringToDate(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        Date date = formatter.parse(str);
        return date;
    }

    public static String formatStringOnlyLocalDateTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDateTime = date.format(formatter); // "1986-04-08 12:30"
        return formattedDateTime;
    }

    public static String formatStringDate(String formatDate, Date date) {
        return new SimpleDateFormat(formatDate).format(date).toString();
    }

    public static LocalDate formatStringToDate(String formatDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateTime = LocalDate.parse(formatDate, formatter);
        return dateTime;
    }
    public static LocalDateTime formatStringToLocalDateTime(String formatDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(formatDate, formatter);
        return dateTime;
    }
    public static String formatNumberWithThousandsSeparators(Double value){
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("de", "DE"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        DecimalFormat df = (DecimalFormat)nf;
//        DecimalFormat formatter = new DecimalFormat("#0.##0,00");
        return df.format(value);
    }

    public static String formatNumberWithThousandsSeparatorsThreeDigits(Double value){
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("de", "DE"));
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        DecimalFormat df = (DecimalFormat)nf;
//        DecimalFormat formatter = new DecimalFormat("#0.##0,00");
        return df.format(value);
    }

    public static String formatBankAccountNumber(String accountNumber){
        String valRep = accountNumber.substring(accountNumber.length()-3,accountNumber.length());
        String replace = accountNumber.substring(0,accountNumber.length()-3);
        replace = replace.replaceAll("[0-9]","*");
        return replace+valRep;
    }

    public static String getActualYear() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String fullName(String name, String lastName, String motherLastName, String marriedLastName){
        String completeName = "";
        if (name != null) completeName += name.trim();
        if (lastName != null) completeName += " " + lastName.trim();
        if (motherLastName != null) completeName += " " + motherLastName.trim();
        completeName += (marriedLastName == null || marriedLastName.isEmpty() || marriedLastName.trim().isEmpty()) ? "" : " " + marriedLastName.trim();
        return completeName;
    }

    public static String fullName(String name, String lastName, String motherLastName, String marriedLastName, long maritalStatus){
        String completeName = "";
        if (name != null) completeName += name.trim();
        if (lastName != null) completeName += " " + lastName.trim();
        if (motherLastName != null) completeName += " " + motherLastName.trim();
        completeName += (marriedLastName == null || marriedLastName.isEmpty() || marriedLastName.trim().isEmpty()) ? "" :
                (ClassifierEnum.WIDOWED_STATUS.getReferenceCode() == maritalStatus) ? " VIUDA DE " + marriedLastName.trim() : " DE " + marriedLastName.trim();
        return completeName;
    }

    public static Integer getAgeRoundedDown(double age) {
        double roundDown = Math.floor(age);
        return (int) roundDown;
    }

    public static int getPageInitRange(int page, int size) {
        int initRange = 0;

        if (page > 0) {
            initRange = page * size;
        }
        return initRange;
    }

    public static void throwExceptionIfInvalidText(String name, String value, boolean required, int maxLength) throws OperationException {
        throwExceptionIfInvalidText(name, value, required, 0, maxLength);
    }
    public static void throwExceptionIfInvalidTexts(String name, String value, boolean required) throws OperationException {
        throwExceptionIfInvalidText(name, value, required, 0, 0);
    }

    public static void throwExceptionIfInvalidText(String name, String value, boolean required, int minLength, int maxLength) throws OperationException {
        if (!required && (value == null || value.trim().length() == 0)) {
            return;
        }
        if (required && (value == null || value.trim().length() == 0)) {
            throw new OperationException("El Campo es Requerido");
        }
        if (minLength > 0 && value.trim().length() < minLength) {
            throw new OperationException("Para el campo '" + name + "' " + value + " la longitud mínima debe ser " + minLength);
        }
        if(maxLength != 0) {
            if (value.trim().length() > maxLength) {
                throw new OperationException("Para el campo '" + name + "' " + value + " la longitud máxima debe ser " + maxLength);
            }
        }
    }

    public static void throwExceptionIfInvalidNumber(String name, Integer value, boolean required, Integer greaterThan) throws OperationException {
        throwExceptionIfInvalidNumber(name, value, required, greaterThan, null);
    }
    public static void throwExceptionIfInvalidNumber(String name, Integer value, boolean required, Integer greaterThan, Integer lessThan) throws OperationException {
        if (!required && value == null)
            return;

        if (required && value == null)
            throw new OperationException("El campo '" + name + "' es requerido");

        if (value.compareTo(greaterThan) <= 0) { // value es menor o igual
            throw new OperationException("El campo '" + name + "' debe ser mayor que " + greaterThan);
        }

        if (lessThan != null && value.compareTo(lessThan) >= 0) { // value es mayor o igual
            throw new OperationException("El campo '" + name + "' debe ser menor que " + lessThan);
        }
    }

    public static void throwExceptionIfInvalidNumber(String name, Long value, boolean required, Long greaterThan) throws OperationException {
        throwExceptionIfInvalidNumber(name, value, required, greaterThan, null);
    }
    public static void throwExceptionIfInvalidNumber(String name, Long value, boolean required, Long greaterThan, Long lessThan) throws OperationException {
        if (!required && value == null)
            return;

        if (required && value == null)
            throw new OperationException("El campo '" + name + "' es requerido");

        if (value.compareTo(greaterThan) <= 0) { // value es menor o igual
            throw new OperationException("El campo '" + name + "' debe ser mayor que " + greaterThan);
        }

        if (lessThan != null && value.compareTo(lessThan) >= 0) { // value es mayor o igual
            throw new OperationException("El campo '" + name + "' debe ser menor que " + lessThan);
        }
    }

    public static void throwExceptionRequiredIfNull(String name, Object value) throws OperationException {
        if (value == null) {
            throw new OperationException("El campo '" + name + "' es requerido");
        }
    }


}
