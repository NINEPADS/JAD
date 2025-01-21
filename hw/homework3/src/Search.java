import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * Search类提供了在Lucene索引目录中进行不同类型搜索的方法。
 */
public class Search {
    FSDirectory dictionary; // 存储索引的目录
    IndexReader indexReader; // 用于读取索引的IndexReader
    IndexSearcher indexSearcher; // 用于执行搜索操作的IndexSearcher
    QueryParser parser; // 用于解析查询字符串的QueryParser

    /**
     * 构造函数初始化搜索环境，打开指定目录并设置必要的组件以进行搜索。
     * @throws Exception 如果打开目录或初始化搜索组件时发生错误
     */
    public Search() throws Exception {
        dictionary = FSDirectory.open(Paths.get(".\\resource\\results"));
        indexReader = DirectoryReader.open(dictionary);
        indexSearcher = new IndexSearcher(indexReader);
        parser = new QueryParser("content", new StandardAnalyzer()); 
    }

    /**
     * 根据关键词进行精确搜索。
     * @param keyWords 要搜索的关键词
     * @throws Exception 如果搜索过程中发生错误
     */
    public void searchKeyWord(String keyWords) throws Exception {
        Query termQuery = new TermQuery(new Term("content", keyWords)); // 创建TermQuery对象
        TopDocs docs = indexSearcher.search(termQuery, 5); // 执行搜索，返回前5个结果
        System.out.println(docs.totalHits); // 输出总命中数
        ScoreDoc[] scoreDocs = docs.scoreDocs; // 获取得分文档数组
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc; // 获取文档ID
            Document document = indexReader.document(docId); // 读取文档内容
            System.out.println("path: \n" + document.getField("path").stringValue()); // 输出文档路径
            System.out.println("score:\n" + scoreDoc.score); // 输出文档得分
            //System.out.println("content: \n" + document.getField("content")); // 输出文档内容（注释掉）
        }
    }

    /**
     * 根据句子进行搜索，使用AND操作符连接各个词项。
     * @param sentence 要搜索的句子
     * @throws Exception 如果搜索过程中发生错误
     */
    public void searchSentence(String sentence) throws Exception {
        parser.setDefaultOperator(QueryParser.AND_OPERATOR); // 设置默认操作符为AND
        Query sentenceQuery = parser.createPhraseQuery("content", sentence); // 解析查询字符串
        TopDocs docs = indexSearcher.search(sentenceQuery, 5); // 执行搜索，返回前5个结果
        System.out.println(docs.totalHits); // 输出总命中数
        ScoreDoc[] scoreDocs = docs.scoreDocs; // 获取得分文档数组
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc; // 获取文档ID
            Document document = indexReader.document(docId); // 读取文档内容
            System.out.println("path: \n" + document.getField("path").stringValue()); // 输出文档路径
            System.out.println("score:\n" + scoreDoc.score); // 输出文档得分
        }
    }

    /**
     * 根据关键词进行模糊搜索。
     * @param keyWord 要搜索的关键词
     * @throws Exception 如果搜索过程中发生错误
     */
    public void searchKeyWordFuzzily(String keyWord) throws Exception {
        Query fuzzyQuery = new FuzzyQuery(new Term("content", keyWord)); // 创建FuzzyQuery对象
        TopDocs docs = indexSearcher.search(fuzzyQuery, 5); // 执行搜索，返回前5个结果
        System.out.println(docs.totalHits); // 输出总命中数
        ScoreDoc[] scoreDocs = docs.scoreDocs; // 获取得分文档数组
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc; // 获取文档ID
            Document document = indexReader.document(docId); // 读取文档内容
            System.out.println("path: \n" + document.getField("path").stringValue()); // 输出文档路径
            System.out.println("score:\n" + scoreDoc.score); // 输出文档得分
            //System.out.println("content: \n" + document.getField("content")); // 输出文档内容（注释掉）
        }
    }
}
