package winexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileExplorer {
	 JFrame frame;
	JPopupMenu popupMenu;
	JMenuItem extractItem;
	JButton btnextract;
	JButton btngiainen;
	String selected;
	FileTreeModel model;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	 new FileExplorer();
	}

	/**
	 * Create the application.
	 */
	public FileExplorer() {
		addItem();
	}
	public void addItem()
	{
		frame= new JFrame("File Explorer");
		 File root= new File("D:\\");
		
		 model = new FileTreeModel(root);
		
	    JTree tree = new JTree(model);
	
		
	    tree.addTreeSelectionListener(new TreeSelectionListener() {
	    	 public void valueChanged(TreeSelectionEvent event) {
	    		 try {
	    		 	selected = tree.getLastSelectedPathComponent().toString();
	    		 		btnextract.setVisible(true);
	    		 		if(selected.endsWith("zip")) {
	    		 			btngiainen.setVisible(true);
	    		 		}
	    	 }
	    		 catch (NullPointerException e) {
					// TODO: handle exception
	    			 JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi chọn file");
				}
	    	 }
	    	    });
	    JScrollPane scrollpane = new JScrollPane(tree);
	    frame.getContentPane().add(scrollpane);
		
		
		
	    JPanel p1= new JPanel();
	   
	    btnextract= new JButton("Delete");
	    btngiainen = new JButton("Extract");
	    p1.add(btnextract);
	    p1.add(btngiainen);
	    btnextract.setVisible(false);
	    btngiainen.setVisible(false);
	    btnextract.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				File file= new File(selected);
				file.delete();
				frame.dispose();
				addItem();
				
			}
		});
	    btngiainen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String out= selected.replaceAll(".zip", "");
				extractZip(selected, out);
				frame.dispose();
				addItem();
			}
		});
	    frame.add(p1,"South");
	    frame.setSize(400, 600);
	    frame.setVisible(true);
	}
	    
		public void extractZip(String file, String output) {
			File folder= new File(output);
			 if (!folder.exists()) {
		            folder.mkdirs();
		        }
			 byte[] buffer= new byte[1024];
			 ZipInputStream zip= null;
			 
			 try {
				 zip= new ZipInputStream(new FileInputStream(file));
				 ZipEntry entry = null;
				while((entry=zip.getNextEntry())!=null) {
					String entryName= entry.getName();
					String outFileName= output+File.separator+entryName;
					JOptionPane.showMessageDialog(null, "File "+outFileName+" đã được giải nén tại "+ output);
					if (entry.isDirectory()) {
						// tạo thư mục 
						new File(outFileName).mkdirs();
					}
					else {
						FileOutputStream fos= new FileOutputStream(outFileName);
						  int len;
		                    // Đọc dữ liệu trên Entry hiện tại.
		                    while ((len = zip.read(buffer)) > 0) {
		                        fos.write(buffer, 0, len);
		                    }
		 
							fos.close();
					
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
			}
		}
	}
	
class FileTreeModel implements TreeModel{
	protected File root;
	public FileTreeModel (File root) {
		this.root=root;
	}

public Object getRoot() {
	return root;
}
public boolean isLeaf(Object node) {
	return ((File)node).isFile();
}
public int getChildCount(Object parent) {

	String[] chidren= ((File)parent).list(); // luu danh sÃ¡ch m?c con vÃ o m?ng chidren
	if(chidren==null) return 0; // tr? v? s? lu?ng m?c con
	else return chidren.length;
}

public Object getChild(Object parent, int index) {
//Tr? v? v? trÃ­ file index,
	String[] children= ((File) parent).list();
	if(children==null ||index >= children.length )
		return null;
	else return new File((File) parent, children[index]);// tr? v? file con
}
public int getIndexOfChild(Object parent, Object child) {
	// TÃ¬m v? trÃ­ th? t? c?a m?c con trong m?c cha
	String[] children = ((File)parent).list();
    if (children == null) return -1;
    String childname = ((File)child).getName();
    for(int i = 0; i < children.length; i++) {
      if (childname.equals(children[i])) return i;
    }
    return -1;
  }

public void valueForPathChanged(TreePath path, Object newvalue) {}
public void addTreeModelListener(TreeModelListener l) {}
public void removeTreeModelListener(TreeModelListener l) {}
}