package component;

/**
 * Dialog的枚举
 */
public enum DialogResult {
    RESULT_OK(1),
    RESULT_CANCEL(0),
    RESULT_YES(3),
    RESULT_NO(4);


    int value;
    DialogResult(int v){
        value=v;
    }
}
