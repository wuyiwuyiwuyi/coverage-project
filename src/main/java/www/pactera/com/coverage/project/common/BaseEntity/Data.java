package www.pactera.com.coverage.project.common.BaseEntity;

import java.util.Arrays;

public class Data<T> {

    private T[] data;

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    public Data(T[] data) {
        this.data = data;
    }

    public Data() {

    }

    @Override
    public String toString() {
        return "Data{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
