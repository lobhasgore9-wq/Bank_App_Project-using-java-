import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
class BankAccount {
    String username, password;
    double balance;
    BankAccount(String u, String p) { username=u; password=p; balance=0; }
}
public class BankApp extends JFrame {
    HashMap<String, BankAccount> accounts = new HashMap<>();
    BankAccount current;
    CardLayout cl = new CardLayout();
    JPanel mainPanel = new JPanel(cl);
    JTextField loginUser = new JTextField(15), regUser = new JTextField(15), amtField = new JTextField(10);
    JPasswordField loginPass = new JPasswordField(15), regPass = new JPasswordField(15);
    JLabel welcomeLabel = new JLabel("Welcome, User", SwingConstants.CENTER),
           balanceLabel = new JLabel("Balance: Rs.0", SwingConstants.CENTER);
    JTextArea history = new JTextArea();
    public BankApp() {
        setTitle("Smart Bank");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel bankName = new JLabel("SMART BANK", SwingConstants.CENTER);
        bankName.setFont(new Font("Arial", Font.BOLD, 36));
        bankName.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(bankName, BorderLayout.CENTER);
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createDashboardPanel(), "Dashboard");
        JPanel container = new JPanel(new BorderLayout());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(mainPanel, BorderLayout.CENTER);
        setContentPane(container);
        cl.show(mainPanel, "Login");
    }
    JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel card = new JPanel(new GridLayout(5, 1, 10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));
        JLabel title = new JLabel("Bank Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        JButton loginBtn = new JButton("Login"), goRegisterBtn = new JButton("New User? Register");
        loginBtn.addActionListener(e -> login());
        goRegisterBtn.addActionListener(e -> cl.show(mainPanel, "Register"));
        loginUser.setBorder(BorderFactory.createTitledBorder("Username"));
        loginPass.setBorder(BorderFactory.createTitledBorder("Password"));
        card.add(title);
        card.add(loginUser);
        card.add(loginPass);
        card.add(loginBtn);
        card.add(goRegisterBtn);
        panel.add(card);
        return panel;
    }
    JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel card = new JPanel(new GridLayout(5, 1, 10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));
        JLabel regTitle = new JLabel("New User Registration", SwingConstants.CENTER);
        regTitle.setFont(new Font("Arial", Font.BOLD, 22));
        JButton createBtn = new JButton("Create Account"), backBtn = new JButton("Back to Login");
        createBtn.addActionListener(e -> createAccount());
        backBtn.addActionListener(e -> { clearFields(regUser, regPass); cl.show(mainPanel, "Login"); });
        regUser.setBorder(BorderFactory.createTitledBorder("Create a Username"));
        regPass.setBorder(BorderFactory.createTitledBorder("Create a Password"));
        card.add(regTitle); card.add(regUser); card.add(regPass); card.add(createBtn); card.add(backBtn);
        panel.add(card);
        return panel;
    }
    JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel dashCard = new JPanel(new GridLayout(6, 1, 10, 10));
        dashCard.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dashCard.add(welcomeLabel); dashCard.add(balanceLabel);
        amtField.setBorder(BorderFactory.createTitledBorder("Enter Amount"));
        dashCard.add(amtField);
        JButton dep = new JButton("Deposit"), with = new JButton("Withdraw"),
                chk = new JButton("Check Balance"), out = new JButton("Logout");
        dep.addActionListener(e -> deposit());
        with.addActionListener(e -> withdraw());
        chk.addActionListener(e -> updateBalance("📊 Balance: Rs." + current.balance));
        out.addActionListener(e -> logout());
        JPanel btnRow1 = new JPanel(new GridLayout(1, 2, 10, 10));
        btnRow1.add(dep); btnRow1.add(with);
        JPanel btnRow2 = new JPanel(new GridLayout(1, 2, 10, 10));
        btnRow2.add(chk); btnRow2.add(out);
        dashCard.add(btnRow1); dashCard.add(btnRow2);
        history.setEditable(false);
        history.setLineWrap(true);
        history.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(history);
        scroll.setPreferredSize(new Dimension(250, 400));
        scroll.setBorder(BorderFactory.createTitledBorder("📜 Transactions"));
        panel.add(scroll, BorderLayout.WEST);
        panel.add(dashCard, BorderLayout.CENTER);
        return panel;
    }

    void login() {
        String user = loginUser.getText().trim();
        String pass = new String(loginPass.getPassword());
        if (accounts.containsKey(user) && accounts.get(user).password.equals(pass)) {
            current = accounts.get(user);
            welcomeLabel.setText("👋 Welcome, " + user);
            updateBalance("✅ Logged in successfully!");
            cl.show(mainPanel, "Dashboard");
        } else msg("❌ Invalid Login!");
    }

    void createAccount() {
        String user = regUser.getText().trim(), pass = new String(regPass.getPassword());
        if (!accounts.containsKey(user) && !user.isEmpty() && !pass.isEmpty()) {
            accounts.put(user, new BankAccount(user, pass));
            msg("✅ Account Created! Please login.");
            clearFields(regUser, regPass);
            cl.show(mainPanel, "Login");
        } else msg("⚠ Username taken or empty fields!");
    }
    void deposit() {
        try {
            double amt = Double.parseDouble(amtField.getText());
            current.balance += amt;
            updateBalance("💰 Deposited Rs." + amt);
        } catch (Exception e) { msg("⚠ Enter valid amount!"); }
    }
    void withdraw() {
        try {
            double amt = Double.parseDouble(amtField.getText());
            if (amt <= current.balance) {
                current.balance -= amt;
                updateBalance("💸 Withdrawn Rs." + amt);
            } else updateBalance("⚠ Not enough balance!");
        } catch (Exception e) { msg("⚠ Enter valid amount!"); }
    }
    void logout() {
        clearFields(loginUser, loginPass, amtField);
        cl.show(mainPanel, "Login");
        msg("👋 Logged out!");
    }

    void updateBalance(String text) {
        balanceLabel.setText("Balance: Rs." + current.balance);
        history.append(text + "\n");
        history.setCaretPosition(history.getDocument().getLength());
        amtField.setText("");
    }

    void clearFields(JTextField... fields) { for (JTextField f : fields) f.setText(""); }
    void msg(String text) { JOptionPane.showMessageDialog(this, text); }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankApp().setVisible(true));
    }
}
