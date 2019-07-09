package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class TableUtil {

    public static <T> Node createPage(TableView<T> tableView, ObservableList<T> data, Pagination pagination,
            Integer pageIndex, Integer rowSize) {
        Integer fromIndex = pageIndex * rowSize;
        Integer toIndex = Math.min(fromIndex + rowSize, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        pagination.setPageCount((data.size() / rowSize + 1));

        return new BorderPane();
    }

}
