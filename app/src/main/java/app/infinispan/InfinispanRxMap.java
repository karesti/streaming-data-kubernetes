package app.infinispan;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import java.util.Map;

public interface InfinispanRxMap<K, V> {

  Completable put(K key, V value);

  Maybe<V> get(K key);

  Single<Integer> size();

  Flowable<Map.Entry<K, V>> continuousQuery(String query);

  Completable removeContinuousQueries();

  <T> Flowable<T> query(String query, Map<String, Object> queryParams);

  Completable clear();

  Completable close();

  static <K, V> Single<InfinispanRxMap<K, V>> create(
    String cacheName
    , ConfigurationBuilder cfg
    , Vertx vertx
  ) {
    return Utils.getRemoteCache(cacheName, cfg, vertx)
      .map(
        cache ->
          new RxMapImpl<>((RemoteCache<K, V>) cache, vertx)
      );
  }

  static <K, V> Single<InfinispanRxMap<K, V>> createIndexed(
    String cacheName
    , Class<?>[] indexed
    , ConfigurationBuilder cfg
    , Vertx vertx
  ) {
    return Utils.getIndexedRemoteCache(cacheName, indexed, cfg, vertx)
      .map(
        cache ->
          new RxMapImpl<>((RemoteCache<K, V>) cache, vertx)
      );
  }

}
