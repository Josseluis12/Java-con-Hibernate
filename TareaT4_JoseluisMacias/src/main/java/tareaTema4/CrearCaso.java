package tareaTema4;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CrearCaso extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldTipoDelito;
    private JTextField textFieldEstado;
    private JLabel lblTipoDelito;
    private JLabel lblEstado;
    private JLabel lblFechaInicio;
    private JLabel lblJuez;
    private JComboBox<String> comboBoxJuez;
    private JTextField textFieldFechaInicio;

    public CrearCaso() {
        setTitle("Crear Caso");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 479, 443);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("NUEVO CASO");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel.setBounds(51, 35, 326, 37);
        getContentPane().add(lblNewLabel);

        lblTipoDelito = new JLabel("Tipo de Delito");
        lblTipoDelito.setBounds(51, 117, 97, 13);
        getContentPane().add(lblTipoDelito);

        lblEstado = new JLabel("Estado");
        lblEstado.setBounds(51, 164, 61, 13);
        getContentPane().add(lblEstado);

        lblFechaInicio = new JLabel("Fecha de Inicio");
        lblFechaInicio.setBounds(51, 207, 113, 16);
        getContentPane().add(lblFechaInicio);

        lblJuez = new JLabel("Juez");
        lblJuez.setBounds(51, 76, 45, 13);
        getContentPane().add(lblJuez);

        comboBoxJuez = new JComboBox<String>();
        comboBoxJuez.setBounds(152, 72, 258, 21);
        getContentPane().add(comboBoxJuez);

        textFieldTipoDelito = new JTextField();
        textFieldTipoDelito.setColumns(10);
        textFieldTipoDelito.setBounds(152, 114, 258, 19);
        getContentPane().add(textFieldTipoDelito);

        textFieldEstado = new JTextField();
        textFieldEstado.setColumns(10);
        textFieldEstado.setBounds(152, 161, 258, 19);
        getContentPane().add(textFieldEstado);

        textFieldFechaInicio = new JTextField();
        textFieldFechaInicio.setColumns(10);
        textFieldFechaInicio.setBounds(152, 204, 96, 19);
        getContentPane().add(textFieldFechaInicio);

        JButton btnGuardarCaso = new JButton("GUARDAR");
        btnGuardarCaso.setBounds(51, 255, 129, 37);
        getContentPane().add(btnGuardarCaso);

        btnGuardarCaso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarCaso();
            }
        });

        fillComboBoxJueces(); // Llenamos el ComboBox con los jueces disponibles
    }

    private void guardarCaso() {
        String tipoDelito = textFieldTipoDelito.getText();
        String estado = textFieldEstado.getText();
        String fechaInicioString = textFieldFechaInicio.getText();
        Date fechaInicio = null;
        try {
            fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicioString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Obtenemos el nombre y apellido del juez seleccionado del ComboBox
        String nombreCompletoJuez = comboBoxJuez.getSelectedItem().toString();
        String[] partesNombre = nombreCompletoJuez.split(" ");
        String nombreJuez = partesNombre[0];
        String apellidoJuez = partesNombre[1];

        // Utilizamos el método obtenerJuez() para cargar el objeto Juez desde la base de datos
        Juez juez = obtenerJuez(nombreJuez, apellidoJuez);

        // Creamos el objeto Caso con el juez y demás información proporcionada por el usuario
        Caso caso = new Caso(juez, tipoDelito, fechaInicio, estado);

        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(caso);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Juez obtenerJuez(String nombreJuez, String apellidoJuez) {
        Juez juez = null;
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            String query = "FROM Juez WHERE nombre = :nombre AND apellido = :apellido";
            juez = session.createQuery(query, Juez.class)
                    .setParameter("nombre", nombreJuez)
                    .setParameter("apellido", apellidoJuez)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juez;
    }

    private void fillComboBoxJueces() {
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            List<Juez> jueces = session.createQuery("FROM Juez", Juez.class).getResultList();
            for (Juez juez : jueces) {
                comboBoxJuez.addItem(juez.getNombre() + " " + juez.getApellido());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
