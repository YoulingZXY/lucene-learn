package com.youling.lucenelearn.Service.impl;

import com.youling.lucenelearn.Service.ProductService;
import com.youling.lucenelearn.entity.Product;
import com.youling.lucenelearn.repository.ProductRepository;
import com.youling.lucenelearn.util.LuceneUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void add(Product product) {
        productRepository.save(product);
        IndexWriter indexWriter = LuceneUtil.getIndexWriter("pro");
        Document document = new Document();
        document.add(new IntField("id",product.getId(), Field.Store.YES));
        TextField textField = new TextField("name", product.getName(), Field.Store.YES);
        //加权
        //textField.setBoost(10f);
        document.add(textField);
        document.add(new DoubleField("price",product.getPrice(), Field.Store.YES));
        document.add(new StringField("location",product.getLocation(), Field.Store.NO));
        document.add(new IntField("categoryId",product.getCategoryId(),Field.Store.YES));
        try {
            indexWriter.addDocument(document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        productRepository.save(product);
        IndexWriter indexWriter = LuceneUtil.getIndexWriter("pro");
        Document document = new Document();
        document.add(new IntField("id",product.getId(), Field.Store.YES));
        document.add(new TextField("name",product.getName(), Field.Store.YES));
        document.add(new DoubleField("price",product.getPrice(), Field.Store.YES));
        document.add(new StringField("location",product.getLocation(), Field.Store.NO));
        document.add(new IntField("categoryId",product.getCategoryId(),Field.Store.YES));
        try {
            indexWriter.updateDocument(new Term("id",product.getId()+""), document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> queryAll(Query query,Integer pageNumber,Integer pageSize) {

        //分页相关数据，如果不传，则设置默认值
        if(pageNumber==null){
            pageNumber=1;
        }
        if(pageSize==null){
            pageSize=5;
        }

        //需要加入高亮器的标签样式 参数1：起始标签，参数2：结束标签
        Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        //关键词
        Scorer scorer = new QueryTermScorer(query);
        //创建高亮器对象
        Highlighter highlighter = new Highlighter(formatter,scorer);


        List<Product> productList = new ArrayList<>();
        //获取索引搜索对象
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher("pro");
        /*//查询条件，~~~基于词的查询，~~~词源
        Query query = new TermQuery(new Term("name", field));*/
        try {
            TopDocs topDocs = indexSearcher.search(query, pageNumber*pageSize);
            //相关度的排序数组(当前关键词查询出来的所有对象的 id 的数组)，score越大，排名越前
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = (pageNumber-1)*pageSize; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                //拿到当前对象的 id
                int doc = scoreDoc.doc;
                //获取当前索引的score
                float score = scoreDoc.score;
                Document document = indexSearcher.doc(doc);
                Product product = new Product();
                product.setPrice(Double.parseDouble(document.get("price")));
                //
                //product.setName(document.get("name"));
                //添加高亮效果
                try {
                    //防止高亮处理后没有高亮关键词的空指针异常
                    if(highlighter.getBestFragment(new IKAnalyzer(), "name", document.get("name"))==null){
                        product.setName(document.get("name"));
                    }else{
                        product.setName(highlighter.getBestFragment(new IKAnalyzer(), "name", document.get("name")));
                    }
                } catch (InvalidTokenOffsetsException e) {
                    e.printStackTrace();
                }
                product.setId(Integer.parseInt(document.get("id")));
                product.setCategoryId(Integer.parseInt(document.get("categoryId")));
                productList.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
