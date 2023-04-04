package drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import geometry.Line;
import geometry.Point;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

@SuppressWarnings("serial")
public class DlgLine extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JTextField txtStartPointX;
	private JTextField txtStartPointY;
	private JTextField txtEndPointY;
	private JTextField txtEndPointX;
	
	private JButton btnColor;
	
	private boolean confirmation;
	private boolean colorConfirmation;
	
	Color fill;
	
	Line l;

	public static void main(String[] args) {
		try {
			DlgLine dialog = new DlgLine();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DlgLine() {
		setModal(true);
		setTitle("Modify");
		setBounds(100, 100, 300, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(0, 0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblStartPointX = new JLabel("Start point X:");
			lblStartPointX.setForeground(new Color(0, 255, 102));
			lblStartPointX.setFont(new Font("Times New Roman", Font.BOLD, 12));
			GridBagConstraints gbc_lblStartPointX = new GridBagConstraints();
			gbc_lblStartPointX.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartPointX.gridx = 1;
			gbc_lblStartPointX.gridy = 1;
			contentPanel.add(lblStartPointX, gbc_lblStartPointX);
		}
		{
			txtStartPointX = new JTextField();
			txtStartPointX.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			txtStartPointX.setBackground(new Color(0, 255, 102));
			txtStartPointX.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_txtStartPointX = new GridBagConstraints();
			gbc_txtStartPointX.gridwidth = 2;
			gbc_txtStartPointX.insets = new Insets(0, 0, 5, 5);
			gbc_txtStartPointX.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtStartPointX.gridx = 3;
			gbc_txtStartPointX.gridy = 1;
			contentPanel.add(txtStartPointX, gbc_txtStartPointX);
			txtStartPointX.setColumns(10);
		}
		{
			JLabel lblStartPointY = new JLabel("Start point Y:");
			lblStartPointY.setForeground(new Color(0, 255, 102));
			lblStartPointY.setFont(new Font("Times New Roman", Font.BOLD, 12));
			GridBagConstraints gbc_lblStartPointY = new GridBagConstraints();
			gbc_lblStartPointY.insets = new Insets(0, 0, 5, 5);
			gbc_lblStartPointY.gridx = 1;
			gbc_lblStartPointY.gridy = 2;
			contentPanel.add(lblStartPointY, gbc_lblStartPointY);
		}
		{
			txtStartPointY = new JTextField();
			txtStartPointY.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			txtStartPointY.setBackground(new Color(0, 255, 102));
			txtStartPointY.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_txtStartPointY = new GridBagConstraints();
			gbc_txtStartPointY.gridwidth = 2;
			gbc_txtStartPointY.insets = new Insets(0, 0, 5, 5);
			gbc_txtStartPointY.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtStartPointY.gridx = 3;
			gbc_txtStartPointY.gridy = 2;
			contentPanel.add(txtStartPointY, gbc_txtStartPointY);
			txtStartPointY.setColumns(10);
		}
		{
			JLabel lblEndPointX = new JLabel("End point X:");
			lblEndPointX.setForeground(new Color(0, 255, 102));
			lblEndPointX.setFont(new Font("Times New Roman", Font.BOLD, 12));
			GridBagConstraints gbc_lblEndPointX = new GridBagConstraints();
			gbc_lblEndPointX.insets = new Insets(0, 0, 5, 5);
			gbc_lblEndPointX.gridx = 1;
			gbc_lblEndPointX.gridy = 3;
			contentPanel.add(lblEndPointX, gbc_lblEndPointX);
		}
		{
			txtEndPointX = new JTextField();
			txtEndPointX.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			txtEndPointX.setBackground(new Color(0, 255, 102));
			txtEndPointX.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_txtEndPointX = new GridBagConstraints();
			gbc_txtEndPointX.gridwidth = 2;
			gbc_txtEndPointX.insets = new Insets(0, 0, 5, 5);
			gbc_txtEndPointX.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtEndPointX.gridx = 3;
			gbc_txtEndPointX.gridy = 3;
			contentPanel.add(txtEndPointX, gbc_txtEndPointX);
			txtEndPointX.setColumns(10);
		}
		{
			JLabel lblEndPointY = new JLabel("End point Y:");
			lblEndPointY.setForeground(new Color(0, 255, 102));
			lblEndPointY.setFont(new Font("Times New Roman", Font.BOLD, 12));
			GridBagConstraints gbc_lblEndPointY = new GridBagConstraints();
			gbc_lblEndPointY.insets = new Insets(0, 0, 5, 5);
			gbc_lblEndPointY.gridx = 1;
			gbc_lblEndPointY.gridy = 4;
			contentPanel.add(lblEndPointY, gbc_lblEndPointY);
		}
		{
			txtEndPointY = new JTextField();
			txtEndPointY.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			txtEndPointY.setBackground(new Color(0, 255, 102));
			txtEndPointY.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_txtEndPointY = new GridBagConstraints();
			gbc_txtEndPointY.gridwidth = 2;
			gbc_txtEndPointY.insets = new Insets(0, 0, 5, 5);
			gbc_txtEndPointY.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtEndPointY.gridx = 3;
			gbc_txtEndPointY.gridy = 4;
			contentPanel.add(txtEndPointY, gbc_txtEndPointY);
			txtEndPointY.setColumns(10);
		}
		
//________________________________ Color _______________________________________
		
		{
			btnColor = new JButton("Color");
			btnColor.setBackground(new Color(0, 255, 102));
			btnColor.setFont(new Font("Times New Roman", Font.BOLD, 12));
			btnColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) 
				{
					fill = JColorChooser.showDialog(null, "Choose a color", fill);
					btnColor.setBackground(fill);
					colorConfirmation = true;
				}
			});
			GridBagConstraints gbc_btnColor = new GridBagConstraints();
			gbc_btnColor.insets = new Insets(0, 0, 0, 5);
			gbc_btnColor.gridx = 1;
			gbc_btnColor.gridy = 6;
			contentPanel.add(btnColor, gbc_btnColor);
		}
		
//____________________________ OK / Cancel ___________________________________
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(0, 255, 102));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setForeground(new Color(0, 255, 102));
				okButton.setBackground(new Color(0, 0, 0));
				okButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) 
					{
						try
						{
							int newStartPointX = Integer.parseInt(txtStartPointX.getText());
							int newStartPointY = Integer.parseInt(txtStartPointY.getText());
							int newEndPointX = Integer.parseInt(txtEndPointX.getText());
							int newEndPointY = Integer.parseInt(txtEndPointY.getText());
									
							if (newStartPointX < 0 || newStartPointY < 0 || newEndPointX < 0 || newEndPointY < 0)
							{
								JOptionPane.showMessageDialog(null, "Enter/Wrong value!", "Error", JOptionPane.ERROR_MESSAGE);
							}
							else 
							{
								l = new Line(new Point(newStartPointX, newStartPointY), new Point(newEndPointX, newEndPointY), fill);
								confirmation = true;
								dispose();
							}
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(null, "Enter/Wrong value!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setForeground(new Color(0, 255, 102));
				cancelButton.setBackground(new Color(0, 0, 0));
				cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

//__________________________ Getters and Setters _______________________________
	
	public JTextField getTxtStartPointX() {
		return txtStartPointX;
	}

	public void setTxtStartPointX(JTextField txtStartPointX) {
		this.txtStartPointX = txtStartPointX;
	}

	public JTextField getTxtStartPointY() {
		return txtStartPointY;
	}

	public void setTxtStartPointY(JTextField txtStartPointY) {
		this.txtStartPointY = txtStartPointY;
	}

	public JTextField getTxtEndPointY() {
		return txtEndPointY;
	}

	public void setTxtEndPointY(JTextField txtEndPointY) {
		this.txtEndPointY = txtEndPointY;
	}

	public JTextField getTxtEndPointX() {
		return txtEndPointX;
	}

	public void setTxtEndPointX(JTextField txtEndPointX) {
		this.txtEndPointX = txtEndPointX;
	}

	public boolean isConfirmation() {
		return confirmation;
	}

	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}

	public boolean isColorConfirmation() {
		return colorConfirmation;
	}

	public void setColorConfirmation(boolean colorConfirmation) {
		this.colorConfirmation = colorConfirmation;
	}

	public Color getFill() {
		return fill;
	}

	public void setFill(Color fill) {
		this.fill = fill;
	}

	public JButton getBtnColor() {
		return btnColor;
	}

	public void setBtnColor(JButton btnColor) {
		this.btnColor = btnColor;
	}
}
