//package data.composition.factory.data;
//
//import data.composition.factory.source.Source;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.function.BiConsumer;
//
///**
// * @author zhangjinyu
// * @since 2023-07-25
// */
//public class MapData<K, V> implements Data<Map<K, V>> {
//    private final Map<K, V> data;
//    private final List<Source<Map<K, V>, ?>> sourceList;
//
//    public MapData(Map<K, V> data) {
//        this.data = data;
//        sourceList = new ArrayList<>();
//    }
//
//    public <S> Data<Map<K, V>> from(Source<Map<K, V>, S> source) {
//        if (Objects.nonNull(source)) {
//            sourceList.add(source);
//        }
//        return this;
//    }
//
//    @Override
//    public void composition() {
//        if (Objects.isNull(data)) {
//            return;
//        }
//        data.forEach(new BiConsumer<K, V>() {
//            @Override
//            public void accept(K k, V v) {
//                System.out.println(k + ":" + v);
//            }
//        });
//    }
//
//    @Override
//    public Map<K, V> compositionNew() {
//        if (Objects.isNull(data)) {
//            return null;
//        }
//        return null;
//    }
//
//}
