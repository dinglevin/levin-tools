package levin.analysis.financial.data.provider;


public interface DataProvider<T, U> {
    T provide(U arg);
}
