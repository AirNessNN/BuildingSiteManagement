package dbManager;

public interface ISearchable {
    Object findAt(int index);

    Object find(Object object);

    Object indexOf(Object object);
}
