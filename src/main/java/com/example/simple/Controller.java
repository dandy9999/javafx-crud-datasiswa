package com.example.simple;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

import java.sql.*;

public class Controller {

    private static final String INSERT_QUERY = "INSERT INTO datasiswa (nama, kelas, alamat, hobi) VALUES (?, ?, ?, ?)";
    private static final String SEARCH_QUERY = "SELECT * FROM datasiswa WHERE nama LIKE ? OR kelas LIKE ? OR alamat LIKE ? OR hobi LIKE ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM datasiswa";
    private static final String UPDATE_QUERY = "UPDATE datasiswa SET nama = ?, kelas = ?, alamat = ?, hobi = ? WHERE id = ? LIMIT 1";
    private static final String DELETE_QUERY = "DELETE FROM datasiswa WHERE id = ?";

    private int idsel = 0;

    @FXML
    private TextField alamatArea;

    @FXML
    private Button cariButton;

    @FXML
    private TextField cariField;

    @FXML
    private Button editBtn;

    @FXML
    private Button hapusBtn;

    @FXML
    private TextField hobiArea;

    @FXML
    private TextField kelasArea;

    @FXML
    private Label labelApp;

    @FXML
    private TextField namaArea;

    @FXML
    private TableColumn<Siswa, String> tableAlamatSiswa;

    @FXML
    private TableColumn<Siswa, String> tableHobiSiswa;

    @FXML
    private TableColumn<Siswa, Integer> tableIdSiswa;

    @FXML
    private TableColumn<Siswa, String> tableKelasSiswa;

    @FXML
    private TableColumn<Siswa, String> tableNamaSiswa;

    @FXML
    private TableView<Siswa> tableSiswa;

    @FXML
    private Button tambahButton;

    @FXML
    void btnTambahClicked(ActionEvent event) {
        Window owner = tambahButton.getScene().getWindow();
        if (namaArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "tambah data gagal!",
                    "masukkan nama");
            return;
        }
        if (kelasArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "tambah data gagal!",
                    "masukkan kelas");
            return;
        }
        if (alamatArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "tambah data gagal!",
                    "masukkan alamat");
            return;
        }
        if (hobiArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "tambah data gagal!",
                    "masukkan hobi");
            return;
        }
        String nama = namaArea.getText();
        String kelas = kelasArea.getText();
        String alamat = alamatArea.getText();
        String hobi = hobiArea.getText();
        if (labelApp.getText() == "Edit Data Siswa"){
            updateData(idsel, nama, kelas, alamat, hobi);
            labelApp.setText("Tambah Data Siswa");
        }
        else {
            inputData(nama, kelas, alamat, hobi);
        }

        showAlert(Alert.AlertType.INFORMATION, owner, "tambah/edit data berhasil!",
                "data siswa berhasil diedit/ditambahkan!");
        populateSiswa();
    }
    @FXML
    void cariClicked(ActionEvent event) {
        Window owner = cariButton.getScene().getWindow();
        if (cariField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "cari data gagal!",
                    "masukkan nama siswa");
            return;
        }
        populateSiswa(cariField.getText());
    }
    @FXML
    void editClicked(ActionEvent event) {
        Window owner = editBtn.getScene().getWindow();
        Siswa sw = tableSiswa.getSelectionModel().getSelectedItem();
        if (tableSiswa.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "gagal!",
                    "pilih salah satu siswa di tabel");
            return;
        }
        labelApp.setText("Edit Data Siswa");
        idsel = sw.getId();
        namaArea.setText(sw.getNama());
        kelasArea.setText(sw.getKelas());
        alamatArea.setText(sw.getAlamat());
        hobiArea.setText(sw.getHobi());
    }

    @FXML
    void hapusClicked(ActionEvent event) {
        Window owner = hapusBtn.getScene().getWindow();
        Siswa sw = tableSiswa.getSelectionModel().getSelectedItem();
        if (tableSiswa.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "gagal!",
                    "pilih salah satu siswa di tabel");
            return;
        }
        idsel = sw.getId();
        hapusData(idsel);
        showAlert(Alert.AlertType.INFORMATION, owner, "hapus data berhasil!",
                "data siswa '"+sw.getNama()+"' berhasil dihapus!");
        populateSiswa();
    }

    @FXML
    void refreshClicked(ActionEvent event) {
        populateSiswa();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void inputData(String nama, String kelas, String alamat, String hobi) {
        DBConnect kon = new DBConnect();
        try (Connection conn = kon.getConnection();
             PreparedStatement pst = conn.prepareStatement(INSERT_QUERY))
        {
            pst.setString(1, nama);
            pst.setString(2, kelas);
            pst.setString(3, alamat);
            pst.setString(4, hobi);

            System.out.println(pst);
            pst.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public void updateData(int id, String nama, String kelas, String alamat, String hobi) {
        DBConnect kon = new DBConnect();
        try (Connection conn = kon.getConnection();
             PreparedStatement pst = conn.prepareStatement(UPDATE_QUERY))
        {
            pst.setString(1, nama);
            pst.setString(2, kelas);
            pst.setString(3, alamat);
            pst.setString(4, hobi);
            pst.setInt(5, id);

            System.out.println(pst);
            pst.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public void hapusData(int id) {
        DBConnect kon = new DBConnect();
        try (Connection conn = kon.getConnection();
             PreparedStatement pst = conn.prepareStatement(DELETE_QUERY))
        {
            pst.setInt(1, id);

            System.out.println(pst);
            pst.execute();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public ObservableList<Siswa> getSiswaList(){
        ObservableList<Siswa> siswaList = FXCollections.observableArrayList();
        DBConnect kon = new DBConnect();
        Connection conn = kon.getConnection();
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(GET_ALL_QUERY);
            Siswa siswa;

            while(rs.next()){
                siswa = new Siswa(rs.getInt("id"), rs.getString("nama"), rs.getString("kelas"),rs.getString("alamat"), rs.getString("hobi"));
                siswaList.add(siswa);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return siswaList;
    }
    public ObservableList<Siswa> searchSiswa(String keyword){
        ObservableList<Siswa> siswaList = FXCollections.observableArrayList();
        DBConnect kon = new DBConnect();
        Connection conn = kon.getConnection();
        PreparedStatement pst;
        ResultSet rs;
        try{
            pst = conn.prepareStatement(SEARCH_QUERY);
            for (int x=1; x<=4; x++){
                pst.setString(x,"%"+keyword+"%");
            }
            rs = pst.executeQuery();
            Siswa siswa;

            while(rs.next()){
                siswa = new Siswa(rs.getInt("id"), rs.getString("nama"), rs.getString("kelas"),rs.getString("alamat"), rs.getString("hobi"));
                siswaList.add(siswa);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return siswaList;
    }
    public void populateSiswa(String keyword){
        ObservableList<Siswa> list = getSiswaList();
        if (keyword!=null){
            list = searchSiswa(keyword);
        }
        tableIdSiswa.setCellValueFactory(new PropertyValueFactory<Siswa, Integer>("id"));
        tableNamaSiswa.setCellValueFactory(new PropertyValueFactory<Siswa, String>("nama"));
        tableKelasSiswa.setCellValueFactory(new PropertyValueFactory<Siswa, String>("kelas"));
        tableAlamatSiswa.setCellValueFactory(new PropertyValueFactory<Siswa, String>("alamat"));
        tableHobiSiswa.setCellValueFactory(new PropertyValueFactory<Siswa, String>("hobi"));

        tableSiswa.setItems(list);
    }
    //overloading
    public void populateSiswa(){
        populateSiswa(null);
    }
    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
