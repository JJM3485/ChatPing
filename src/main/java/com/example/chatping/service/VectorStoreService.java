package com.example.chatping.service;

import com.example.chatping.entity.Product;
import com.example.chatping.repository.ProductRepository;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorStoreService {

    private final ProductRepository productRepository;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    // 서버 시작 시 딱 한 번 실행됨!
    @PostConstruct
    public void initVectorStore() {
        // 1. DB에서 모든 상품 가져오기
        List<Product> products = productRepository.findAll();

        // 2. 상품 하나하나를 "문서(Document)"로 만들어서 저장
        for (Product product : products) {
            // AI가 검색할 텍스트: "상품명 + 설명 + 카테고리"
            String text = "상품명: " + product.getName() + "\n" +
                    "가격: " + product.getPrice() + "원\n" +
                    "설명: " + product.getDescription() + "\n" +
                    "카테고리: " + product.getCategory();

            // 문서 생성 (Metadata로 ID와 가격을 넣어둠)
            Document document = Document.from(text);
            document.metadata().put("id", product.getId().toString());
            document.metadata().put("price", String.valueOf(product.getPrice()));

            // 3. 임베딩 + 저장 (Ingestor가 알아서 다 해줌)
            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            ingestor.ingest(document);
        }

        System.out.println("✅ AI 벡터 저장소에 상품 " + products.size() + "개 등록 완료!");
    }
}
