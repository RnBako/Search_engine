import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class FIOForm {
    private JPanel FIOPanel;
    private JButton collapseExpandButton;
    private JTextField lastnameTextField;
    private JTextField firstnameTextField;
    private JTextField middlenameTextField;
    private JLabel lastnameLabel;
    private JLabel firstnameLabel;
    private JLabel middlenameLabel;
    private JLabel FIOLabel;
    private JTextField FIOTextField;

    public FIOForm () {
        collapseExpandButton.addActionListener(new Action() {
            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public void putValue(String key, Object value) {

            }

            @Override
            public void setEnabled(boolean b) {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (collapseExpandButton.getText().equals("Свернуть")) {
                    if (lastnameTextField.getText().isEmpty() || firstnameTextField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                FIOPanel,
                                "Не заполнены Фамилия или Имя!",
                                "Не заполнены поля",
                                JOptionPane.PLAIN_MESSAGE
                        );
                    } else {
                        lastnameLabel.setVisible(false);
                        lastnameTextField.setVisible(false);

                        firstnameLabel.setVisible(false);
                        firstnameTextField.setVisible(false);

                        middlenameLabel.setVisible(false);
                        middlenameTextField.setVisible(false);

                        FIOTextField.setText(lastnameTextField.getText() + " " + firstnameTextField.getText() + " " + middlenameTextField.getText());
                        FIOLabel.setVisible(true);
                        FIOTextField.setVisible(true);

                        collapseExpandButton.setText("Развернуть");
                    }
                } else if (collapseExpandButton.getText().equals("Развернуть")) {
                    String FIO = FIOTextField.getText().trim();
                    if (FIO.matches("[0-ё]+\\s[0-ё]+")) {
                        String lastname = FIO.substring(0, FIO.indexOf(" "));
                        FIO = FIO.substring(lastname.length()).trim();
                        lastnameTextField.setText(lastname);
                        lastnameLabel.setVisible(true);
                        lastnameTextField.setVisible(true);

                        firstnameTextField.setText(FIO);
                        firstnameLabel.setVisible(true);
                        firstnameTextField.setVisible(true);

                        middlenameLabel.setVisible(true);
                        middlenameTextField.setVisible(true);

                        FIOLabel.setVisible(false);
                        FIOTextField.setVisible(false);

                        collapseExpandButton.setText("Свернуть");
                    } else if (FIO.matches("[0-ё]+\\s[0-ё]+\\s[0-ё]+")) {
                        String lastname = FIO.substring(0, FIO.indexOf(" "));
                        FIO = FIO.substring(lastname.length()).trim();
                        lastnameTextField.setText(lastname);
                        lastnameLabel.setVisible(true);
                        lastnameTextField.setVisible(true);

                        String firstname = FIO.substring(0, FIO.indexOf(" "));
                        FIO = FIO.substring(firstname.length()).trim();
                        firstnameTextField.setText(firstname);
                        firstnameLabel.setVisible(true);
                        firstnameTextField.setVisible(true);

                        middlenameTextField.setText(FIO);
                        middlenameLabel.setVisible(true);
                        middlenameTextField.setVisible(true);

                        FIOLabel.setVisible(false);
                        FIOTextField.setVisible(false);

                        collapseExpandButton.setText("Свернуть");
                    } else {
                        JOptionPane.showMessageDialog(
                                FIOPanel,
                                "Не верно заполнено ФИО! ",
                                "Не заполнены поля",
                                JOptionPane.PLAIN_MESSAGE
                        );
                    }
                }
            }
        });
    }

    public JPanel getFIOPanel() {
        return FIOPanel;
    }
}
