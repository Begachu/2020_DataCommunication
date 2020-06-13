package testGUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jnetpcap.PcapIf;

public class ChatFileDlg extends JFrame {

	public int nUpperLayerCount = 0;
	public String pLayerName = null;

	private JTextField ChattingWrite;

	Container contentPane;

	JTextArea ChattingArea; // 챗팅화면 보여주는 위치
	JTextArea srcMacAddress;
	JTextArea dstMacAddress;

	JLabel lblsrc; // Label(이름)
	JLabel lbldst;

	JButton Setting_Button; // Port번호(주소)를 입력받은 후 완료버튼설정
	JButton Chat_send_Button; // 채팅화면의 채팅 입력 완료 후 data Send버튼
	JButton Setting_file_Button;
	JButton File_send_Button;
	
	JTextField fileNameField;
	JProgressBar file_progress;

	static JComboBox<String> NICComboBox;

	int adapterNumber = 0;

	String Text;
	public Object progressBar;

	public static void main(String[] args) {
		new ChatFileDlg("test");
	}

	public ChatFileDlg(String pName) {
		pLayerName = pName;

		setTitle("Stop & Wait Protocol");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 250, 644, 425);
		contentPane = new JPanel();
		((JComponent) contentPane).setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel chattingPanel = new JPanel();// chatting panel
		chattingPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "채팅", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		chattingPanel.setBounds(10, 5, 360, 276);
		contentPane.add(chattingPanel);
		chattingPanel.setLayout(null);

		JPanel chattingEditorPanel = new JPanel();// chatting write panel
		chattingEditorPanel.setBounds(10, 15, 340, 210);
		chattingPanel.add(chattingEditorPanel);
		chattingEditorPanel.setLayout(null);

		ChattingArea = new JTextArea();
		ChattingArea.setEditable(false);
		ChattingArea.setBounds(0, 0, 340, 210);
		chattingEditorPanel.add(ChattingArea);// chatting edit

		JPanel chattingInputPanel = new JPanel();// chatting write panel
		chattingInputPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		chattingInputPanel.setBounds(10, 230, 250, 20);
		chattingPanel.add(chattingInputPanel);
		chattingInputPanel.setLayout(null);

		ChattingWrite = new JTextField();
		ChattingWrite.setBounds(2, 2, 250, 20);// 249
		chattingInputPanel.add(ChattingWrite);
		ChattingWrite.setColumns(10);// writing area

		// file panel
		JPanel filePanel = new JPanel();// file panel
		filePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "파일 전송",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		filePanel.setBounds(10, 290, 360, 85);
		contentPane.add(filePanel);
		filePanel.setLayout(null);

		fileNameField = new JTextField();
		fileNameField.setBounds(10, 20, 260, 25);
		filePanel.add(fileNameField);
		fileNameField.setEnabled(false);
		
		this.Setting_file_Button = new JButton("파일 선택");
		Setting_file_Button.setBounds(275, 20, 75, 25);
		filePanel.add(this.Setting_file_Button);
		
		this.file_progress = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
		this.file_progress.setBounds(10, 50, 260, 25);
		filePanel.add(this.file_progress);
		
		this.File_send_Button = new JButton("전송");
		this.File_send_Button.setBounds(275, 50, 75, 25);
		filePanel.add(this.File_send_Button);
/*
		ChattingArea = new JTextArea();
		ChattingArea.setEditable(false);
		ChattingArea.setBounds(0, 0, 340, 210);
		fileEditorPanel.add(ChattingArea);// chatting edit

		JPanel fileTestPanel = new JPanel();// chatting write panel
		fileTestPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		fileTestPanel.setBounds(10, 230, 250, 20);
		filePanel.add(fileTestPanel);
		fileTestPanel.setLayout(null);

		ChattingWrite = new JTextField();
		ChattingWrite.setBounds(2, 2, 250, 20);// 249
		fileTestPanel.add(ChattingWrite);
		ChattingWrite.setColumns(10);// writing area*/
		// file panel end

		JPanel settingPanel = new JPanel(); // Setting 관련 패널
		settingPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "setting",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		settingPanel.setBounds(380, 5, 236, 371);
		contentPane.add(settingPanel);
		settingPanel.setLayout(null);

		JPanel sourceAddressPanel = new JPanel();
		sourceAddressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sourceAddressPanel.setBounds(10, 140, 170, 20);
		settingPanel.add(sourceAddressPanel);
		sourceAddressPanel.setLayout(null);

		lblsrc = new JLabel("Source Mac Address");
		lblsrc.setBounds(10, 115, 170, 20); // 위치 지정
		settingPanel.add(lblsrc); // panel 추가

		srcMacAddress = new JTextArea();
		srcMacAddress.setBounds(2, 2, 170, 20);
		sourceAddressPanel.add(srcMacAddress);// src address

		JPanel destinationAddressPanel = new JPanel();
		destinationAddressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		destinationAddressPanel.setBounds(10, 212, 170, 20);
		settingPanel.add(destinationAddressPanel);
		destinationAddressPanel.setLayout(null);

		lbldst = new JLabel("Destination Mac Address");
		lbldst.setBounds(10, 187, 190, 20);
		settingPanel.add(lbldst);

		dstMacAddress = new JTextArea();
		dstMacAddress.setBounds(2, 2, 170, 20);
		destinationAddressPanel.add(dstMacAddress);// dst address

		JLabel NICLabel = new JLabel("NIC List");
		NICLabel.setBounds(10, 20, 170, 20);
		settingPanel.add(NICLabel);

		NICComboBox = new JComboBox();
		NICComboBox.setBounds(10, 49, 170, 20);
		settingPanel.add(NICComboBox);


		Setting_Button = new JButton("Setting");// setting
		Setting_Button.setBounds(80, 270, 100, 20);
		Setting_Button.addActionListener(new setAddressListener());
		settingPanel.add(Setting_Button);// setting

		Chat_send_Button = new JButton("Send");
		Chat_send_Button.setBounds(270, 230, 80, 20);
		Chat_send_Button.addActionListener(new setAddressListener());
		filePanel.add(Chat_send_Button);// chatting send button

		setVisible(true);

	}

	class setAddressListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

		}

		public String get_MacAddress(byte[] byte_MacAddress) { // MAC Byte주소를 String으로 변환

			String MacAddress = "";
			for (int i = 0; i < 6; i++) {
				// 2자리 16진수를 대문자로, 그리고 1자리 16진수는 앞에 0을 붙임.
				MacAddress += String.format("%02X%s", byte_MacAddress[i], (i < MacAddress.length() - 1) ? "" : "");

				if (i != 5) {
					// 2자리 16진수 자리 단위 뒤에 "-"붙여주기
					MacAddress += "-";
				}
			}
			System.out.println("mac_address:" + MacAddress);
			return MacAddress;
		}

		public boolean Receive(byte[] input) { // 메시지 Receive
			if (input != null) {
				byte[] data = input; // byte 단위의 input data
				Text = new String(data); // 아래층에서 올라온 메시지를 String text로 변환해줌
				ChattingArea.append("[RECV] : " + Text + "\n"); // 채팅창에 수신메시지를 보여줌
				return false;
			}
			return false;
		}

	}
}
