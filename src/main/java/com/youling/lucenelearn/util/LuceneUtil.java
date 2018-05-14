package com.youling.lucenelearn.util;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class LuceneUtil {
    private static Directory proDirectory;
    private static Directory catDirectory;
    private static Version version;
    private static IndexWriterConfig indexWriterConfig;
    private static Analyzer analyzer;
    private static IndexReader indexReader;

    static {
        try {
            //引擎版本
            version = Version.LUCENE_44;
            //分词器
            analyzer = new IKAnalyzer();
            //索引目录
            proDirectory = FSDirectory.open(new File("E:\\java\\demoTest\\LuceneTest\\pro"));
            catDirectory = FSDirectory.open(new File("E:\\java\\demoTest\\LuceneTest\\cat"));
            //加载配置
            indexWriterConfig = new IndexWriterConfig(version, analyzer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static IndexWriter getIndexWriter(String param) {
        IndexWriter indexWriter = null;
        try {
            if("pro".equals(param)){
                indexWriter = new IndexWriter(proDirectory, indexWriterConfig);
            }else if("cat".equals(param)){
                indexWriter = new IndexWriter(catDirectory, indexWriterConfig);
            }else{
                throw new RuntimeException("创建目录参数缺失（pro Or cat ?）");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }

    public static IndexSearcher getIndexSearcher(String param) {
        IndexSearcher indexSearcher = null;
        try {
            if("pro".equals(param)){
                indexReader = DirectoryReader.open(proDirectory);
            }else if("cat".equals(param)){
                indexReader = DirectoryReader.open(catDirectory);
            }else{
                throw new RuntimeException("查询目录参数缺失（pro Or cat ?）");
            }
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexSearcher;
    }

    public static void commit(IndexWriter indexWriter) {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(IndexWriter indexWriter) {
        try {
            indexWriter.rollback();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
