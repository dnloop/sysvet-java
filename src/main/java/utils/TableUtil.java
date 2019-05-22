package utils;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;

public class TableUtil {

    public static <T extends RecursiveTreeObject<T>> Node createPage(JFXTreeTableView<T> tableView,
            ObservableList<T> data, Pagination pagination, Integer pageIndex, Integer rowSize) {
        Integer fromIndex = pageIndex * rowSize;
        Integer toIndex = Math.min(fromIndex + rowSize, data.size());
        tableView.setRoot(new RecursiveTreeItem<T>(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)),
                RecursiveTreeObject::getChildren));
        pagination.setPageCount((data.size() / rowSize + 1));

        return new BorderPane();
    }

}
