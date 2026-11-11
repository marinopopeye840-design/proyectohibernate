
package ni.edu.uam.proyectohibernate.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.proyectohibernate.dao.CategoriaDao;
import ni.edu.uam.proyectohibernate.dao.LibroDao;
import ni.edu.uam.proyectohibernate.model.Categoria;
import ni.edu.uam.proyectohibernate.model.Libro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibroController {

    @FXML private ComboBox<Categoria> cmbCategoria;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtPrecio;

    @FXML private TableView<Libro> tblLibros;
    @FXML private Button btnModificarLibro;
    @FXML private Button btnEliminarLibro;

    @FXML private TableColumn<Libro, Long> coldID;
    @FXML private TableColumn<Libro, String> coldTitulo;
    @FXML private TableColumn<Libro, String> coldAutor;
    @FXML private TableColumn<Libro, Integer> coldAnio;
    @FXML private TableColumn<Libro, BigDecimal> coldPrecio;
    @FXML private TableColumn<Libro, String> coldCategoria;
    private final CategoriaDao categoriaDao = new CategoriaDao();
    private final LibroDao libroDAO = new LibroDao();

    // ID del registro seleccionado en el tableView.
    private Long idSeleccionado = null;

    public void initialize() {
        coldID.setCellValueFactory(new PropertyValueFactory<>("id"));
        coldTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        coldAutor.setCellValueFactory(new PropertyValueFactory<>("audtor"));
        coldAnio.setCellValueFactory(new PropertyValueFactory<>("aniopPublicacion"));
        coldPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Mostar el nombre, no el objeto Categoría.
        coldCategoria.setCellValueFactory(data -> {;
            Categoria categoria = data.getValue().getCategoria();
            String nombre = categoria != null ? categoria.getNombre() : "Sin categoría";
            return new SimpleStringProperty(nombre);
        });
        tblLibros.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null){
                seleccionarLibro(newValue);
                actualizarBotones();
            }else{
                idSeleccionado = null;
                actualizarBotones();
            }
        });


        tblLibros.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                seleccionarLibro(newValue);
            } else {
                idSeleccionado = null;
                actualizarBotones();
            }
        });

        actualizarBotones();
        cargarLibros();
        cargarCategorias();
    }

    private void seleccionarLibro(Libro libro) {
        idSeleccionado = libro.getId();
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAudtor());
        txtAnio.setText(String.valueOf(libro.getAniopPublicacion()));
        txtPrecio.setText(String.valueOf(libro.getPrecio()));
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean sinSeleccion = idSeleccionado == null;
        btnModificarLibro.setDisable(sinSeleccion);
        btnEliminarLibro.setDisable(sinSeleccion);
    }

    private Libro obtenerLibroFormulario() {
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int anioPublicacion = Integer.valueOf(txtAnio.getText());
        BigDecimal precio = new BigDecimal(txtPrecio.getText());

        Categoria categoria = cmbCategoria.getValue();
        if (categoria == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoría");
        }
        return new Libro(titulo, autor, anioPublicacion, precio, categoria);
    }

    //Recuperar las Categoria de postgreSQL
    private void cargarCategorias(){
        try{
            List<Categoria> categorias = categoriaDao.listar();
            cmbCategoria.setItems(FXCollections.observableArrayList(categorias));
        }catch( Exception e){
            e.printStackTrace();
            mostrarAlert(
                    Alert.AlertType.ERROR,
                    "No se puedieron cargar las categoira "
            );

        }
    }
    @FXML
    private void guardarLibros() {
        try {
            Libro libro = obtenerLibroFormulario();
            libroDAO.guardar(libro);
            limpiarFormulario();
            cargarLibros();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void modificarLibro() {
        try {
            Libro libro = obtenerLibroFormulario();
            libro.setId(idSeleccionado);
            boolean modificado = libroDAO.modificar(libro);

            if (!modificado) {
                // Alert para indicar que hubo un erro a nivel de transaccion
            }

            limpiarFormulario();
            cargarLibros();

            // Alert para indicar que la modificacion fue exitosa

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void eliminarLibro() {
        try {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmacion de la eliminacion de datos");
            confirmacion.setHeaderText("Eliminar Libro");
            confirmacion.setContentText(" Estas seguro que deseas eliminar el registro seleccionado?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
                return;
            }

            boolean eliminado = libroDAO.eliminar(idSeleccionado);

            if (!eliminado) {
                // Alert
            }

            // Elert para identificar que la operacoon fue realizada
            limpiarFormulario();
            cargarLibros();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void cargarLibros(){
        try{
            List<Libro> libros = libroDAO.listar();
            limpiarFormulario();
            tblLibros.setItems(
                    FXCollections.observableArrayList(libros));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void limpiarFormulario(){
        tblLibros.getSelectionModel().clearSelection();
        idSeleccionado = null;
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();
        actualizarBotones();
    }

    private void mostrarAlert(Alert.AlertType type, String message){
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
