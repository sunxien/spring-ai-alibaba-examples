package com.sunxien.examples.features.memory;

import com.alibaba.cloud.ai.graph.store.NamespaceListRequest;
import com.alibaba.cloud.ai.graph.store.StoreItem;
import com.alibaba.cloud.ai.graph.store.StoreSearchRequest;
import com.alibaba.cloud.ai.graph.store.StoreSearchResult;
import com.alibaba.cloud.ai.graph.store.stores.BaseStore;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
public class MySqlStore extends BaseStore {



    @Override
    public void putItem(StoreItem item) {
    }

    @Override
    public Optional<StoreItem> getItem(List<String> namespace, String key) {
        return Optional.empty();
    }

    @Override
    public boolean deleteItem(List<String> namespace, String key) {
        return false;
    }

    @Override
    public StoreSearchResult searchItems(StoreSearchRequest searchRequest) {
        return null;
    }

    @Override
    public List<String> listNamespaces(NamespaceListRequest namespaceRequest) {
        return List.of();
    }

    @Override
    public void clear() {
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
