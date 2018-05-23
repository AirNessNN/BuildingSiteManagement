package dbManager.dbInterface;

public interface ISearchable {
    Object findAt(int index);

    Object find(Object object);

    Object indexOf(Object object);
}
