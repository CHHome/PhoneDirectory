package com.phone.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.phone.controller.DbController;

public class PhoneView extends JFrame{
	private JTable myTable;
	private String[] tableTitle  = {"姓名","描述","手机号码","家庭电话","地址"};
	private JPanel jpanel = new JPanel();
	private  JButton delete = new JButton("删除选中联系人");
	private  JButton add = new JButton("增加联系人");
	
	private  JButton update = new JButton("修改联系人");
	private  JButton updateComfirm = new JButton("修改");
	private  JButton updateCancle = new JButton("取消");
	private JDialog updateDlg = new JDialog(this,"更新联系人");//更新联系人
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
		
		showData("id");//第一次显示所有记录，并初始化conn 
		this.setTitle("电话薄");
		this.setSize(800,600);
		this.setLocation(350,100);
		this.setLayout(new FlowLayout());
		myModel = new DefaultTableModel(showList,this.tableTitle);
		myTable = new JTable(myModel);
		JFrame content = new JFrame();
		jpanel.add(delete);
		jpanel.add(add);
		jpanel.add(update);
		this.add(new JScrollPane(myTable));
		this.add(jpanel);
		initUpdateDlg();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//更新数据
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//updateDlg.setModal(true);不能在这里设置，会阻塞
				updateName.setText(null);
				updateDsc.setText(null);
				updateMobile.setText(null);
				updateHomePhone.setText(null);
				updateAddress.setText(null);
				int selectItem = myTable.getSelectedRow();
				if(selectItem==-1){
					System.out.println("请选择要修改的记录");
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
				if(dbcontroler.update(id, name, description, mobilenumber, homenumber, address)){
					System.out.println("删除成功");//使用错误dialog
					showData("id");
					myModel = new DefaultTableModel(showList,tableTitle);
					myTable.setModel(myModel);
				}
				else
					System.out.println("删除失败");//使用错误dialog
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
						System.out.println("删除成功");//待替换错误dialog
						myModel.removeRow(selectList[i]-flag);
						showData("id");//重新加载渲染数据
						flag++;
					}else{
						System.out.println("删除失败");//待替换错误dialog
					}
				}
				if(id==null){
					System.out.println("请选择要删除的记录");//待替换错误dialog
				}
				
			}	
		});
		
		
		//添加数据
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String[] newRow = {"张文超","3383236","15625506831","东莞松山湖"};
				myModel.addRow(newRow);
			}
		});
		
	}
	
	public void initUpdateDlg(){
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
		jpanelbt.add(updateComfirm);
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
	public void showData(String order){
		try {
			int i =0;
			dbcontroler = new DbController();
			sqlRet = dbcontroler.selectAll(order);
			showList = new String[getListLength(sqlRet)][];
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
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new PhoneView();

	}

}
