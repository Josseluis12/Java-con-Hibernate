package tareaTema4;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CrearJuez extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextField textFieldNombreJuez;
    private JTextField textFieldApellidoJuez;
    private JTextField textFieldAreaEspecializacion;

    public CrearJuez() {
        setTitle("Crear Juez");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 479, 300);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("NUEVO JUEZ");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel.setBounds(51, 35, 326, 37);
        getContentPane().add(lblNewLabel);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(51, 90, 45, 13);
        getContentPane().add(lblNombre);

        JLabel lblApellido = new JLabel("Apellido");
        lblApellido.setBounds(51, 137, 61, 13);
        getContentPane().add(lblApellido);

        JLabel lblAreaEspecializacion = new JLabel("Área Especialización");
        lblAreaEspecializacion.setBounds(51, 180, 121, 13);
        getContentPane().add(lblAreaEspecializacion);

        textFieldNombreJuez = new JTextField();
        textFieldNombreJuez.setBounds(159, 87, 258, 19);
        getContentPane().add(textFieldNombreJuez);
        textFieldNombreJuez.setColumns(10);

        textFieldApellidoJuez = new JTextField();
        textFieldApellidoJuez.setColumns(10);
        textFieldApellidoJuez.setBounds(159, 134, 258, 19);
        getContentPane().add(textFieldApellidoJuez);

        textFieldAreaEspecializacion = new JTextField();
        textFieldAreaEspecializacion.setColumns(10);
        textFieldAreaEspecializacion.setBounds(182, 177, 235, 19);
        getContentPane().add(textFieldAreaEspecializacion);

        JButton btnGuardarJuez = new JButton("GUARDAR");
        btnGuardarJuez.setBounds(51, 224, 129, 37);
        getContentPane().add(btnGuardarJuez);

        btnGuardarJuez.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarJuez();
                dispose();
            }
        });
    }

    private void guardarJuez() {
        String nombre = textFieldNombreJuez.getText();
        String apellido = textFieldApellidoJuez.getText();
        String areaEspecializacion = textFieldAreaEspecializacion.getText();

        Juez juez = new Juez(nombre, apellido, areaEspecializacion);

        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(juez);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CrearJuez window = new CrearJuez();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
