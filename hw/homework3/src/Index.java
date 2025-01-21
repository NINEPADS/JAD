import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Field.Store;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;

/**
 * Index类提供了将指定目录下的文件内容建立索引的方法。
 */
public class Index {
    // 定义实现文本转换需要的变量
    static String IDX_DIR = ".\\resource\\results"; // 索引存储目录
    static String DOC_DIR = ".\\resource\\docs"; // 文档目录
    static Metadata metadata = new Metadata(); // Tika解析元数据
    static ParseContext parseContext = new ParseContext(); // Tika解析上下文
    static HtmlParser htmlParser = new HtmlParser(); // HTML解析器
    static OOXMLParser ooxmlParser = new OOXMLParser(); // OOXML解析器（如docx）
    static PDFParser pdfParser = new PDFParser(); // PDF解析器

    /**
     * 创建索引的方法。
     * @throws Exception 如果发生错误则抛出异常。
     */
    public static void create() throws Exception {
        FSDirectory idxDir = FSDirectory.open(Paths.get(IDX_DIR)); // 打开索引目录
        Analyzer analyzer = new StandardAnalyzer(); // 使用标准分析器
        IndexWriterConfig config = new IndexWriterConfig(analyzer); // 配置索引写入器
        IndexWriter indexWriter = new IndexWriter(idxDir, config); // 创建索引写入器
        indexWriter.deleteAll(); // 删除所有现有索引
        File[] files = new File(DOC_DIR).listFiles(); // 获取文档目录下的所有文件
        for (File file : files) {
            if (!file.isDirectory()) {
                System.out.println(file); // 打印文件路径
                String fileName = file.toString(); // 获取文件名
                String suffix = fileName.substring(fileName.lastIndexOf('.')); // 获取文件后缀
                switch (suffix) {
                    case ".pdf":
                        insertFileIntoIndex(fileName, new FileInputStream(file), pdfParser, indexWriter); // 处理PDF文件
                        break;
                    case ".html":
                        insertFileIntoIndex(fileName, new FileInputStream(file), htmlParser, indexWriter); // 处理HTML文件
                        break;
                    case ".docx":
                        insertFileIntoIndex(fileName, new FileInputStream(file), ooxmlParser, indexWriter); // 处理DOCX文件
                        break;
                    default:
                        System.out.println("Cannot solve this type: " + suffix); // 无法处理的文件类型
                        break;
                }
            }
        }
        indexWriter.close(); // 关闭索引写入器
    }

    /**
     * 将文件内容插入到索引中。
     * @param filePath 文件路径。
     * @param fileInputStream 文件输入流。
     * @param parser 使用的解析器。
     * @param indexWriter 索引写入器。
     * @throws Exception 如果发生错误则抛出异常。
     */
    public static void insertFileIntoIndex(String filePath, FileInputStream fileInputStream, Parser parser, IndexWriter indexWriter) throws Exception {
        BodyContentHandler handler = new BodyContentHandler(1024 * 1024 * 10); // 设置内容处理器缓冲区大小
        parser.parse(fileInputStream, handler, metadata, parseContext); // 解析文件内容
        Document document = new Document(); // 创建文档对象
        document.add(new StringField("path", filePath, Store.YES)); // 添加文件路径字段
        document.add(new TextField("content", handler.toString(), Store.YES)); // 添加文件内容字段
        indexWriter.updateDocument(new Term("path", filePath), document); // 更新索引中的文档
        indexWriter.commit(); // 提交更改
    }

    /**
     * 从索引中移除文件。
     * @param filePath 文件路径。
     * @param parser 使用的解析器。
     * @param indexWriter 索引写入器。
     * @throws Exception 如果发生错误则抛出异常。
     */
    public static void removeFileFromIndex(String filePath, Parser parser, IndexWriter indexWriter) throws Exception {
        indexWriter.deleteDocuments(new Term("path", filePath)); // 根据文件路径删除索引中的文档
    }
}
