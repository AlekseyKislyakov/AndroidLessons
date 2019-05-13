package kislyakov.a12_1.ui.base;

public interface Presenter<V extends MvpView> {
    void attachView(V mvpView);

    void detachView();
}
