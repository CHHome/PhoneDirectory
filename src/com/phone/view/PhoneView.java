package com.phone.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.phone.controller.DbController;

public class PhoneView extends JFrame{
	private JTable myTable;
	private String[] tableTitle  = {"����","����","�ֻ�����","��ͥ�绰","��ַ"};
	private JPanel jpanel = new JPanel();
	private JButton delete = new JButton("ɾ��ѡ����ϵ��");
	
	private JButton add = new JButton("�½���ϵ��");
	private JButton addComfirm = new JButton("�����ϵ��");
	
	private JButton showAll = new JButton("��ʾȫ��");
	
	private JButton detail = new JButton("�鿴(����)");
	private JButton call = new JButton("����");
	
	private JButton search = new JButton("������ϵ��");
	private JButton searchConfirm = new JButton("����");
	private JComboBox<String> searchType;//�����ؼ���type
	
	private  JButton update = new JButton("�޸���ϵ��");
	private  JButton updateComfirm = new JButton("�޸�");
	private  JButton updateCancle = new JButton("ȡ��");
	private JDialog updateDlg ;//������ϵ��
	
	private JDialog prompt =  new JDialog(this, "��ʾ");//�����ɹ�/������Ϣ��ʾ
	private JButton closePt = new JButton("�ر�");//�ر���ʾ
	private JLabel callBackMsg = new JLabel("");//��ʾ��Ϣ
	
	private JTextField updateName = new JTextField(12);
	private JTextField updateDsc = new JTextField(12);
	private JTextField updateMobile = new JTextField(12);
	private JTextField updateHomePhone = new JTextField(12);
	private JTextField updateAddress = new JTextField(12);
	
	private DefaultTableModel myModel ;
	private DbController dbcontroler;
	private ResultSet sqlRet = null;
	String[][] showList = null;
	
	public PhoneView() {
		
		showData(null,null);//��һ����ʾ���м�¼������ʼ��conn 
		this.setTitle("�绰��");
		this.setSize(800,600);
		this.setLocation(350,100);
		this.setLayout(new FlowLayout());
		myModel = new DefaultTableModel(showList,this.tableTitle);
		myTable = new JTable(myModel){
			   public boolean isCellEditable(int row, int column) { 
			    return false;
			   }
			  };
		jpanel.add(delete);
		jpanel.add(add);
		jpanel.add(update);
		jpanel.add(search);
		jpanel.add(showAll);
		jpanel.add(detail);
		prompt.setLayout(new FlowLayout());
		prompt.add(callBackMsg);
		prompt.add(closePt);
		prompt.setSize(200, 100);
		prompt.setLocation(600, 300);
		this.add(new JScrollPane(myTable));
		this.add(jpanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
		
		//��������
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//updateDlg.setModal(true);�������������ã�������
				setJTextYes();
				initUpdateDlg("������ϵ��",updateComfirm);
				initJText();
				int selectItem = myTable.getSelectedRow();
				if(selectItem==-1){
					callBackMsg.setText("��ѡ��Ҫ�޸ĵļ�¼");
					prompt.setVisible(true);
					return;
				}
				updateName.setText(showList[selectItem][0]);
				updateDsc.setText(showList[selectItem][1]);
				updateMobile.setText(showList[selectItem][2]);
				updateHomePhone.setText(showList[selectItem][3]);
				updateAddress.setText(showList[selectItem][4]);
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//˳���ܱ�
				
			}
		});
		//ȷ�ϸ���
		updateComfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int selectItem = myTable.getSelectedRow();
				String id = showList[selectItem][5];
				String name = updateName.getText();
				String description = updateDsc.getText();
				String mobilenumber = updateMobile.getText();
				String homenumber = updateHomePhone.getText();
				String address = updateAddress.getText();
				System.out.println(name);
				if(name.equals(null)||name.length()==0){
					callBackMsg.setText("����д��ϵ������");
					prompt.setVisible(true);
					updateDlg.dispose();
					return ;
				}
				if(dbcontroler.update(id, name, description, mobilenumber, homenumber, address)){
					callBackMsg.setText("���³ɹ�");
					prompt.setVisible(true);
					showData(null,null);
					myModel = new DefaultTableModel(showList,tableTitle);
					myTable.setModel(myModel);//������Ⱦ
				}
				else{
					callBackMsg.setText("����ʧ��");
					prompt.setVisible(true);
				}
				updateDlg.dispose();
				
			}
		});
		//ȡ������
		updateCancle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				updateDlg.dispose();
			}
		});
		
		//ɾ������
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int[] selectList = myTable.getSelectedRows();
				String id = null;
				int flag =0;//�����һ
				for(int i=0;i<selectList.length;i++){
					id =showList[selectList[i]-flag][5];
					if(dbcontroler.delete(id)){
						callBackMsg.setText("ɾ���ɹ�");
						prompt.setVisible(true);
						myModel.removeRow(selectList[i]-flag);
						showData(null,null);//ͬ���ͻ��˵����ݣ���������δ����setModel
						flag++;
					}else{
						callBackMsg.setText("ɾ��ʧ��");
						prompt.setVisible(true);
					}
				}
				if(id==null){
					callBackMsg.setText("��ѡ��Ҫɾ���ļ�¼");
					prompt.setVisible(true);
				}
				
			}	
		});
		
		
		//�������
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setJTextYes();
				initUpdateDlg("�½���ϵ��",addComfirm);
				initJText();
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//˳���ܱ�
				
				
			}
		});
		
		//ȷ�����
		addComfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name = updateName.getText();
				String description = updateDsc.getText();
				String mobilenumber = updateMobile.getText();
				String homenumber = updateHomePhone.getText();
				String address = updateAddress.getText();
				if(name.equals(null)||name.length()==0){
					callBackMsg.setText("����д��ϵ������");
					prompt.setVisible(true);
					updateDlg.dispose();
					return ;
				}
				if(dbcontroler.insert(name, description, mobilenumber, homenumber, address)){
					showData(null,null);
					myModel = new DefaultTableModel(showList, tableTitle);
					myTable.setModel(myModel);
					callBackMsg.setText("�½���ϵ�˳ɹ�");
					prompt.setVisible(true);
					
				}else{
					callBackMsg.setText("�½���ϵ��ʧ��");
					prompt.setVisible(true);
				}
				updateDlg.dispose();
			}
		});
		//������ϵ��
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setJTextYes();
				initJText();
				initSearch("������ϵ��", searchConfirm);
				updateDlg.setSize(300,150);
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//˳���ܱ�
			}
		});
		
		//ȷ������
		searchConfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String type = (String) searchType.getSelectedItem();
				String col = "name";
				if(type.equals("��������")){
					col = "name";
				}else if(type.equals("�����ֻ�����")){
					col = "mobilenumber";
				}else if(type.equals("������ͥ�绰")){
					col = "homenumber";
				}else if(type.equals("������ַ")){
					col = "address";
				}
				String value = updateName.getText();
				if(value==null||value.length()==0){
					callBackMsg.setText("����д"+type);
					prompt.setVisible(true);
					updateDlg.dispose();
					return ;
				}
				showData(col, value);
				myModel = new DefaultTableModel(showList,tableTitle);
				myTable.setModel(myModel);//������Ⱦ
				updateDlg.dispose();
			}
		});
		//��ʾȫ����ϵ��
		showAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showData(null, null);
				myModel = new DefaultTableModel(showList,tableTitle);
				myTable.setModel(myModel);//������Ⱦ
			}
		});
		
		//�鿴����
		detail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setJTextNo();
				initUpdateDlg("�鿴��ϵ��",call);
				initJText();
				int selectItem = myTable.getSelectedRow();
				if(selectItem==-1){
					callBackMsg.setText("��ѡ��Ҫ�鿴����ϵ��");
					prompt.setVisible(true);
					return;
				}
				updateName.setText(showList[selectItem][0]);
				updateDsc.setText(showList[selectItem][1]);
				updateMobile.setText(showList[selectItem][2]);
				updateHomePhone.setText(showList[selectItem][3]);
				updateAddress.setText(showList[selectItem][4]);
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//˳���ܱ�
			}
		});
		
		call.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateDlg.dispose();
				callBackMsg.setText("���ڲ�����......");
				prompt.setVisible(true);
			}
		});
		
		//�ر���ʾdialog
		closePt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				prompt.dispose();
			}
		});
	}
	
	
	public void initJText(){
		updateName.setText(null);
		updateDsc.setText(null);
		updateMobile.setText(null);
		updateHomePhone.setText(null);
		updateAddress.setText(null);
	}
	
	public void initUpdateDlg(String title, JButton confirm){
		updateDlg = new JDialog(this,title);
		updateDlg.setLayout(new FlowLayout());
		updateDlg.setSize(300,300);
		updateDlg.setLocation(550, 300);
		JLabel labelName = new JLabel("����");
		JLabel labelDSC = new JLabel("����");
		JLabel labelMobile = new JLabel("�ֻ�����");
		JLabel labelHomePhone = new JLabel("��ͥ�绰");
		JLabel labelAddress = new JLabel("סַ");
		JPanel jpanelName = new JPanel();
		JPanel jpanelDSC = new JPanel();
		JPanel jpanelMobile = new JPanel();
		JPanel jpanelHomePhone = new JPanel();
		JPanel jpanelAddress = new JPanel();
		JPanel jpanelbt = new JPanel();
		jpanelName.add(labelName);
		jpanelName.add(updateName);
		jpanelDSC.add(labelDSC);
		jpanelDSC.add(updateDsc);
		jpanelMobile.add(labelMobile);
		jpanelMobile.add(updateMobile);
		jpanelHomePhone.add(labelHomePhone);
		jpanelHomePhone.add(updateHomePhone);
		jpanelAddress.add(labelAddress);
		jpanelAddress.add(updateAddress);
		jpanelbt.add(confirm);
		jpanelbt.add(updateCancle);
		updateDlg.add(jpanelName);
		updateDlg.add(jpanelDSC);
		updateDlg.add(jpanelMobile);
		updateDlg.add(jpanelHomePhone);
		updateDlg.add(jpanelAddress);
		updateDlg.add(jpanelbt);
	}
	
	public int getListLength(ResultSet tempR){
		 int length = 0;
		 try {
			tempR.last();
			length = tempR.getRow();
			tempR.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return length;
		
	}  
	public int showData(String col, String condition){
		int length=0;

		try {
			int i =0;
			dbcontroler = new DbController();
			sqlRet = dbcontroler.select(col, condition);
			if((length = getListLength(sqlRet)) == 0){
				callBackMsg.setText("��ϵ�˲�����");
				prompt.setVisible(true);
				return length;
			}
			showList = new String[length][];
			while(sqlRet.next()){
				String [] item = new String[6];
				item[0] = sqlRet.getString("name");
				item[1] = sqlRet.getString("description");
				item[2] = sqlRet.getString("mobilenumber");
				item[3] = sqlRet.getString("homenumber");
				item[4] = sqlRet.getString("address");
				item[5] = sqlRet.getString("id");
				showList[i] = item;
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return length;
		
	}
	//��ʼ��������
	public void initSearch(String title, JButton confirm){
		searchType = new JComboBox<String>();
		searchType.addItem("��������");
		searchType.addItem("�����ֻ�����");
		searchType.addItem("������ͥ�绰");
		searchType.addItem("������ַ");
		updateDlg = new JDialog(this, title);
		updateDlg.setLayout(new FlowLayout());
		updateDlg.setSize(300,300);
		updateDlg.setLocation(550, 300);
		JPanel jpanelName = new JPanel();
		JPanel jpanelbt = new JPanel();
		jpanelName.add(searchType);
		jpanelName.add(updateName);
		jpanelbt.add(confirm);
		jpanelbt.add(updateCancle);
		updateDlg.add(jpanelName);
		updateDlg.add(jpanelbt);
	}
	//�����ı����Ƿ�ɱ༭
	public void setJTextNo(){
		updateName.setEditable(false);;
		updateDsc.setEditable(false);
		updateMobile.setEditable(false);
		updateHomePhone.setEditable(false);
		updateAddress.setEditable(false);
	}
	public void setJTextYes(){
		updateName.setEditable(true);;
		updateDsc.setEditable(true);
		updateMobile.setEditable(true);
		updateHomePhone.setEditable(true);
		updateAddress.setEditable(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new PhoneView();

	}

}
