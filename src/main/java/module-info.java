module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires java.sql;
    requires jdk.security.jgss;
    opens br.edu.ifsc.fln to javafx.fxml;
    opens br.edu.ifsc.fln.controller to javafx.fxml;
    opens br.edu.ifsc.fln.model.domain to javafx.base;
    exports br.edu.ifsc.fln;
    opens br.edu.ifsc.fln.controller.cadastro to javafx.fxml;
    opens br.edu.ifsc.fln.controller.processo to javafx.fxml;
    opens br.edu.ifsc.fln.controller.graficos to javafx.fxml;
    opens br.edu.ifsc.fln.controller.relatorios to javafx.fxml;
}