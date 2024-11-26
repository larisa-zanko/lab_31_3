//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.*;
import java.util.*;
import java.util.regex.*;


/*Поиск текста на HTML-странице

Постановка задачи

Входные данные:
Входной файл input1.html содержит текст, написанный на языке HTML.
В тесте находятся теги. В одной строке может быть несколько тегов. Теги находятся в угловых скобках, каждому открывающему тегу ставится в соответствие закрывающий тег. Например, пара тегов<b></b>.
Между тегами находится текст, причем теги не разрывают текст. Например, при поиске слова hello комбинация h<b><i>el</i>l</b>o должна быть найдена.
Гарантируется,что страница HTML является корректной, т.е. все символы "<" и ">" используются только в тегах, все теги записаны корректно.
Входной файл input2.in содержит список фрагментов текста, которые нужно найти в первом файле, записанных через разделители (точка с запятой). Может быть несколько строк.

Примечание: Ваша программа должна игнорировать различие между строчными и прописными буквами и для тегов и для искомого контекста.

Выходные данные:
1. В выходной файл output1.out вывести список всех тегов в порядке возрастания количества символов тега.
2. В выходной файл output2.out вывести номера строк (нумерация с 0) первого файла, в которых был найден искомый контекст в первый раз или -1 , если не был найден.
3. В выходной файл output3.out - список фрагментов второго файла, которые НЕ были найдены в первом файле.

Обновлённая задача: 
Входные данные:

Файл input1.html: Содержит текст, написанный на языке HTML. Теги находятся в угловых скобках, и каждый открывающий тег соответствует закрывающему тегу (например, <b></b>). Между тегами находится текст, причем теги не разрывают текст. Например, при поиске слова "hello" комбинация h<b><i>el</i>l</b>o должна быть найдена.
Гарантируется, что HTML-страница является корректной: все символы < и > используются только в тегах, и все теги записаны корректно.
Файл input2.in: Содержит список фрагментов текста, которые нужно найти в первом файле. Фрагменты записаны через разделители (точка с запятой). Может быть несколько строк.
Примечание: Программа должна игнорировать различия между строчными и прописными буквами как для тегов, так и для искомого контекста.

Выходные данные:
В файл output1.out вывести уникальный список всех тегов из файла input1.html, отсортированный по возрастанию длины тегов. Если несколько тегов имеют одинаковую длину, они должны быть отсортированы в алфавитном порядке.
В файл output2.out вывести номера строк (нумерация с 0) файла input1.html, в которых были найдены искомые фрагменты текста из файла input2.in в первый раз. Если фрагмент не найден, вывести -1.
В файл output3.out вывести список фрагментов из input2.in, которые не были найдены в input1.html. Каждый фрагмент должен быть выведен на отдельной строке.
*/

public class Main {
 
    public static void main(String[] args) {

        String htmlFilePath = "input1.html";
        String fragmentsFilePath = "input2.in";
        String outputTagsFilePath = "output1.out";
        String outputLinesFilePath = "output2.out";
        String outputNotFoundFilePath = "output3.out";

        try {
            // Чтение HTML файла
            String htmlContent = readHtmlFile(htmlFilePath);
            List<String> tags = extractTags(htmlContent);

            // Сортировка тегов и запись в файл
            writeSortedTags(tags, outputTagsFilePath);

            // Чтение фрагментов текста
            List<String> fragments = readFragmentsFile(fragmentsFilePath);
            List<Integer> lineNumbers = new ArrayList<>();
            List<String> notFoundFragments = new ArrayList<>();

            // Поиск фрагментов в HTML
            for (String fragment : fragments) {
                int lineNumber = findFragmentInHtml(htmlContent, fragment);
                lineNumbers.add(lineNumber);
                if (lineNumber == -1) {
                    notFoundFragments.add(fragment);
                }
            }

            // Запись результатов в выходные файлы
            writeLineNumbersToFile(lineNumbers, outputLinesFilePath);
            writeNotFoundFragmentsToFile(notFoundFragments, outputNotFoundFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Чтение HTML файла
    private static String readHtmlFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Извлечение тегов из HTML контента
    private static List<String> extractTags(String htmlContent) {
        Set<String> tagSet = new HashSet<>();
        Matcher matcher = Pattern.compile("<[^>]+>").matcher(htmlContent);

        while (matcher.find()) {
            tagSet.add(matcher.group().toLowerCase()); // Игнорируем регистр
        }
        return new ArrayList<>(tagSet);
    }

    // Чтение фрагментов текста из файла
    private static List<String> readFragmentsFile(String filePath) throws IOException {
        List<String> fragments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fragments.addAll(Arrays.asList(line.split(";")));
            }
        }
        return fragments;
    }

    // Поиск фрагмента в HTML контенте
    private static int findFragmentInHtml(String htmlContent, String fragment) {
        String regex = "(?i)" + Pattern.quote(fragment); // Игнорируем регистр и экранируем специальные символы
        Matcher matcher = Pattern.compile(regex).matcher(htmlContent);
        int lineIndex = 0;

        try (BufferedReader br = new BufferedReader(new StringReader(htmlContent))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (matcher.find()) {
                    return lineIndex; // Возвращаем номер строки
                }
                lineIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1; // Если не найдено
    }

    // Запись отсортированных тегов в файл
    private static void writeSortedTags(List<String> tags, String filePath) throws IOException {
        Collections.sort(tags, Comparator.comparingInt(String::length)); // Сортировка по длине
        writeToFile(filePath, tags);
    }

    // Запись номеров строк в файл
    private static void writeLineNumbersToFile(List<Integer> lineNumbers, String filePath) throws IOException {
        writeToFile(filePath, lineNumbers);
    }

    // Запись не найденных фрагментов в файл
    private static void writeNotFoundFragmentsToFile(List<String> notFoundFragments, String filePath) throws IOException {
        writeToFile(filePath, notFoundFragments);
    }

    // Универсальная функция записи данных в файл
    private static <T> void writeToFile(String filePath, List<T> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : data) {
                bw.write(item.toString());
                bw.newLine();
            }
        }
    }
}
