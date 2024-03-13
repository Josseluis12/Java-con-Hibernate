package tareaTema4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main extends JFrame {

    private JComboBox<String> comboBoxJueces;
    private JButton btnFiltrar;
    private JButton btnCrearJuez;
    private JButton btnCrearCaso;
    private JButton btnMostrarCasos;
    private JTable tableCasos;
    private JButton btnBorrarCaso;
    private JButton btnGuardarModificado;

    public Main() {
        setTitle("Gestión de Casos Judiciales");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        getContentPane().add(panelBotones, BorderLayout.NORTH);

        comboBoxJueces = new JComboBox<>();
        btnFiltrar = new JButton("Filtrar");
        btnCrearJuez = new JButton("Crear Juez");
        btnCrearCaso = new JButton("Crear Caso");
        btnMostrarCasos = new JButton("Mostrar Casos");
        btnBorrarCaso = new JButton("Borrar Caso");
        btnGuardarModificado = new JButton("Guardar Modificación");

        panelBotones.add(comboBoxJueces);
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnCrearJuez);
        panelBotones.add(btnCrearCaso);
        panelBotones.add(btnMostrarCasos);
        panelBotones.add(btnBorrarCaso);
        panelBotones.add(btnGuardarModificado);

        btnFiltrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filtrarCasosPorJuez();
            }
        });

        btnCrearJuez.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CrearJuez crearJuez = new CrearJuez();
                crearJuez.setVisible(true);
            }
        });

        btnCrearCaso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CrearCaso crearCaso = new CrearCaso();
                crearCaso.setVisible(true);
            }
        });

        btnMostrarCasos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarCasos();
            }
        });

        btnBorrarCaso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarCasoSeleccionado();
            }
        });

        btnGuardarModificado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarModificaciones();
            }
        });

        tableCasos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableCasos);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Llenar el ComboBox de Jueces al iniciar la aplicación
        llenarComboBoxJueces();

        // Mostrar los casos al iniciar la aplicación
        mostrarCasos();
    }

    private void llenarComboBoxJueces() {
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            List<Juez> jueces = session.createQuery("FROM Juez", Juez.class).list();
            for (Juez juez : jueces) {
                comboBoxJueces.addItem(juez.getNombre() + " " + juez.getApellido());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void filtrarCasosPorJuez() {
        String nombreJuez = (String) comboBoxJueces.getSelectedItem();
        // Obtener el ID del juez seleccionado
        int idJuez = obtenerIdJuez(nombreJuez);
        if (idJuez != -1) {
            try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
                 Session session = factory.openSession()) {
                List<Caso> casos = session.createQuery("FROM Caso WHERE id_juez = :idJuez", Caso.class)
                        .setParameter("idJuez", idJuez)
                        .list();
                actualizarTablaCasos(casos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int obtenerIdJuez(String nombreJuez) {
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            Juez juez = session.createQuery("FROM Juez WHERE CONCAT(nombre_juez, ' ', apellido_juez) = :nombreJuez", Juez.class)
                    .setParameter("nombreJuez", nombreJuez)
                    .uniqueResult();
            if (juez != null) {
                return juez.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void mostrarCasos() {
        actualizarListaJueces(); // Actualizar lista de jueces cada vez que se muestra la lista de casos
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            List<Caso> casos = session.createQuery("FROM Caso", Caso.class).list();
            Object[][] data = new Object[casos.size()][5];
            for (int i = 0; i < casos.size(); i++) {
                Caso caso = casos.get(i);
                data[i][0] = caso.getId();
                data[i][1] = caso.getJuez() != null ? caso.getJuez().getNombre() + " " + caso.getJuez().getApellido() : "Sin asignar";
                data[i][2] = caso.getTipoDelito();
                data[i][3] = caso.getFechaInicio();
                data[i][4] = caso.getEstado();
            }
            String[] columnNames = {"ID", "Juez", "Tipo de Delito", "Fecha de Inicio", "Estado"};
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 2 || column == 3 || column == 4; // Permitir editar solo los campos tipo de delito, fecha de inicio y estado
                }
            };
            tableCasos.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarListaJueces() {
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.openSession()) {
            List<Juez> jueces = session.createQuery("FROM Juez", Juez.class).list();
            DefaultComboBoxModel<String> juezModel = new DefaultComboBoxModel<>();
            for (Juez juez : jueces) {
                juezModel.addElement(juez.getNombre() + " " + juez.getApellido());
            }
            comboBoxJueces.setModel(juezModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void actualizarTablaCasos(List<Caso> casos) {
        DefaultTableModel model = (DefaultTableModel) tableCasos.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de agregar los nuevos casos
        for (Caso caso : casos) {
            Object[] row = {caso.getId(), caso.getJuez().getNombre() + " " + caso.getJuez().getApellido(), caso.getTipoDelito(), caso.getFechaInicio(), caso.getEstado()};
            model.addRow(row);
        }
    }

    private void borrarCasoSeleccionado() {
        int filaSeleccionada = tableCasos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idCaso = (int) tableCasos.getValueAt(filaSeleccionada, 0);
            try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
                 Session session = factory.openSession()) {
                session.beginTransaction();
                Caso caso = session.get(Caso.class, idCaso);
                session.delete(caso);
                session.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            DefaultTableModel model = (DefaultTableModel) tableCasos.getModel();
            model.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(Main.this, "Selecciona un caso para borrar.");
        }
    }

    private void guardarModificaciones() {
        DefaultTableModel model = (DefaultTableModel) tableCasos.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            int idCaso = (int) model.getValueAt(i, 0);
            String tipoDelito = (String) model.getValueAt(i, 2);
            java.util.Date fechaInicio = (java.util.Date) model.getValueAt(i, 3);
            String estado = (String) model.getValueAt(i, 4);

            try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
                 Session session = factory.openSession()) {
                session.beginTransaction();
                Caso caso = session.get(Caso.class, idCaso);
                caso.setTipoDelito(tipoDelito);
                caso.setFechaInicio(fechaInicio);
                caso.setEstado(estado);
                session.update(caso);
                session.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(Main.this, "Modificaciones guardadas exitosamente.");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
