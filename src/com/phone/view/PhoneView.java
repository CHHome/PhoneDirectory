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
	private String[] tableTitle  = {"姓名","描述","手机号码","家庭电话","地址"};
	private JPanel jpanel = new JPanel();
	private JButton delete = new JButton("删除选中联系人");
	
	private JButton add = new JButton("新建联系人");
	private JButton addComfirm = new JButton("添加联系人");
	
	private JButton showAll = new JButton("显示全部");
	
	private JButton detail = new JButton("查看(拨打)");
	private JButton call = new JButton("拨打");
	
	private JButton search = new JButton("搜索联系人");
	private JButton searchConfirm = new JButton("搜索");
	private JComboBox<String> searchType;//搜索关键字type
	
	private  JButton update = new JButton("修改联系人");
	private  JButton updateComfirm = new JButton("修改");
	private  JButton updateCancle = new JButton("取消");
	private JDialog updateDlg ;//更新联系人
	
	private JDialog prompt =  new JDialog(this, "提示");//操作成功/出错信息提示
	private JButton closePt = new JButton("关闭");//关闭提示
	private JLabel callBackMsg = new JLabel("");//提示信息
	
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
		
		showData(null,null);//第一次显示所有记录，并初始化conn 
		this.setTitle("电话薄");
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
		
		
		
		//更新数据
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//updateDlg.setModal(true);不能在这里设置，会阻塞
				setJTextYes();
				initUpdateDlg("更新联系人",updateComfirm);
				initJText();
				int selectItem = myTable.getSelectedRow();
				if(selectItem==-1){
					callBackMsg.setText("请选择要修改的记录");
					prompt.setVisible(true);
					return;
				}
				updateName.setText(showList[selectItem][0]);
				updateDsc.setText(showList[selectItem][1]);
				updateMobile.setText(showList[selectItem][2]);
				updateHomePhone.setText(showList[selectItem][3]);
				updateAddress.setText(showList[selectItem][4]);
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//顺序不能变
				
			}
		});
		//确认更新
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
					callBackMsg.setText("请填写联系人姓名");
					prompt.setVisible(true);
					updateDlg.dispose();
					return ;
				}
				if(dbcontroler.update(id, name, description, mobilenumber, homenumber, address)){
					callBackMsg.setText("更新成功");
					prompt.setVisible(true);
					showData(null,null);
					myModel = new DefaultTableModel(showList,tableTitle);
					myTable.setModel(myModel);//重新渲染
				}
				else{
					callBackMsg.setText("更新失败");
					prompt.setVisible(true);
				}
				updateDlg.dispose();
				
			}
		});
		//取消更新
		updateCancle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				updateDlg.dispose();
			}
		});
		
		//删除数据
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int[] selectList = myTable.getSelectedRows();
				String id = null;
				int flag =0;//首项不减一
				for(int i=0;i<selectList.length;i++){
					id =showList[selectList[i]-flag][5];
					if(dbcontroler.delete(id)){
						callBackMsg.setText("删除成功");
						prompt.setVisible(true);
						myModel.removeRow(selectList[i]-flag);
						showData(null,null);//同步客户端的数据，并非重新未调用setModel
						flag++;
					}else{
						callBackMsg.setText("删除失败");
						prompt.setVisible(true);
					}
				}
				if(id==null){
					callBackMsg.setText("请选择要删除的记录");
					prompt.setVisible(true);
				}
				
			}	
		});
		
		
		//添加数据
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setJTextYes();
				initUpdateDlg("新建联系人",addComfirm);
				initJText();
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//顺序不能变
				
				
			}
		});
		
		//确认添加
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
					callBackMsg.setText("请填写联系人姓名");
					prompt.setVisible(true);
					updateDlg.dispose();
					return ;
				}
				if(dbcontroler.insert(name, description, mobilenumber, homenumber, address)){
					showData(null,null);
					myModel = new DefaultTableModel(showList, tableTitle);
					myTable.setModel(myModel);
					callBackMsg.setText("新建联系人成功");
					prompt.setVisible(true);
					
				}else{
					callBackMsg.setText("新建联系人失败");
					prompt.setVisible(true);
				}
				updateDlg.dispose();
			}
		});
		//搜索联系人
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setJTextYes();
				initJText();
				initSearch("搜索联系人", searchConfirm);
				updateDlg.setSize(300,150);
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//顺序不能变
			}
		});
		
		//确认搜索
		searchConfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String type = (String) searchType.getSelectedItem();
				String col = "name";
				if(type.equals("搜索姓名")){
					col = "name";
				}else if(type.equals("搜索手机号码")){
					col = "mobilenumber";
				}else if(type.equals("搜索家庭电话")){
					col = "homenumber";
				}else if(type.equals("搜索地址")){
					col = "address";
				}
				String value = updateName.getText();
				if(value==null||value.length()==0){
					callBackMsg.setText("请填写"+type);
					prompt.setVisible(true);
					updateDlg.dispose();
					return ;
				}
				showData(col, value);
				myModel = new DefaultTableModel(showList,tableTitle);
				myTable.setModel(myModel);//重新渲染
				updateDlg.dispose();
			}
		});
		//显示全部联系人
		showAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showData(null, null);
				myModel = new DefaultTableModel(showList,tableTitle);
				myTable.setModel(myModel);//重新渲染
			}
		});
		
		//查看拨打
		detail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setJTextNo();
				initUpdateDlg("查看联系人",call);
				initJText();
				int selectItem = myTable.getSelectedRow();
				if(selectItem==-1){
					callBackMsg.setText("请选择要查看的联系人");
					prompt.setVisible(true);
					return;
				}
				updateName.setText(showList[selectItem][0]);
				updateDsc.setText(showList[selectItem][1]);
				updateMobile.setText(showList[selectItem][2]);
				updateHomePhone.setText(showList[selectItem][3]);
				updateAddress.setText(showList[selectItem][4]);
				updateDlg.setModal(true);
				updateDlg.setVisible(true);//顺序不能变
			}
		});
		
		call.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateDlg.dispose();
				callBackMsg.setText("正在拨号中......");
				prompt.setVisible(true);
			}
		});
		
		//关闭提示dialog
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
		JLabel labelName = new JLabel("姓名");
		JLabel labelDSC = new JLabel("描述");
		JLabel labelMobile = new JLabel("手机号码");
		JLabel labelHomePhone = new JLabel("家庭电话");
		JLabel labelAddress = new JLabel("住址");
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
				callBackMsg.setText("联系人不存在");
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
	//初始化搜索框
	public void initSearch(String title, JButton confirm){
		searchType = new JComboBox<String>();
		searchType.addItem("搜索姓名");
		searchType.addItem("搜索手机号码");
		searchType.addItem("搜索家庭电话");
		searchType.addItem("搜索地址");
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
	//设置文本框是否可编辑
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
