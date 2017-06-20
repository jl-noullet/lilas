package lilas.awt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.w3c.dom.Element;

import lilas.Main;
import lilas.base.Module;
import lilas.base.ModuleExternalEvent;
import lilas.base.Signal;
import lilas.base.SignalListener;
import lilas.base.SystemL;
import lilas.util.VectorError;
import lilas.xml.DefAWT;
import lilas.xml.DefModule;
import lilas.xml.DefSignal;
import lilas.xml.DefXML;
//import lilas.awt.treetable.*;	JLN has replaced by 2 lines below
import lilas.awt.treetable.SignalEditorRenderer;
import lilas.awt.treetable.MemoryTableEditorRenderer;

public class MemoryDisplay<T> extends JPanel implements TableModelListener {
	public static final int defaultSizeCol0 = 0;
	public static final int defaultSizeCol1 = 0;
	public SignalEditorRenderer ser;
	JTable jtable;
	
	public MemoryDisplay(Signal<T> mem_signal, Module module) {
		this(mem_signal, new MemoryModel<T>(mem_signal), module, defaultSizeCol0, defaultSizeCol1);
	}

	public MemoryDisplay(Signal<T> mem_signal, Module module, int widthCol0, int widthCol1, int width, int height) {
		this(mem_signal, module, widthCol0, widthCol1);
		//System.out.println(""+this);
		setPreferredSize(new Dimension(width, height));
	}

	public MemoryDisplay(Signal<T> mem_signal, Module module, int widthCol0, int widthCol1) {
		this(mem_signal, new MemoryModel<T>(mem_signal), module, widthCol0, widthCol1);
	}

	public MemoryDisplay(Signal<T> mem_signal, MemoryModel<T> model, Module module) {
		this(mem_signal, model, module, defaultSizeCol0, defaultSizeCol1);
	}
	
	public MemoryDisplay(Signal<T> mem_signal, MemoryModel<T> model, Module module, int widthCol0, int widthCol1, int width, int height) {
		this(mem_signal, model, module, widthCol0, widthCol1);
		setPreferredSize(new Dimension(width, height));
	}
		
	public MemoryDisplay(Signal<T> mem_signal, MemoryModel<T> model, Module module, int widthCol0, int widthCol1) {
		super(new BorderLayout());
		jtable = new JTable();
		JScrollPane scroll = new JScrollPane(jtable);
		add(BorderLayout.CENTER, scroll);
		//add(BorderLayout.CENTER, jtable);
		//Memory<T> model = new Memory<T>("nomDansModule", mem.getMem(), module);
		jtable.setModel(model);
		ser = new MemoryTableEditorRenderer(module.systemL, model); 
		jtable.getColumnModel().getColumn(1).setCellRenderer(ser);
		jtable.getColumnModel().getColumn(1).setCellEditor(ser);
		if (widthCol0==0 || widthCol1==0) jtable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		else {
			jtable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			jtable.getColumnModel().getColumn(0).setPreferredWidth(widthCol0);
			jtable.getColumnModel().getColumn(1).setPreferredWidth(widthCol1);
		}
		
		//setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		jtable.setShowGrid(true);
		model.addTableModelListener(this);
		//jtable.setSize(new Dimension(50,50));
		//Main.msg(""+jtable.getColumnModel().getColumn(0).getPreferredWidth()+" "+jtable.getColumnModel().getColumn(1).getPreferredWidth());
		//Main.msg(""+jtable.getWidth());
	}

	public static Vector<DefAWT> initListDefAWT(Element elAwt, DefModule defModule) {
		return null;
	}
	
	public static void toJavaVarData(DefAWT defAwt, PrintWriter out, String varName) throws IOException {
		String nomSignalMem = defAwt.getElement().getAttribute("memory");
		DefSignal def = (DefSignal) defAwt.getParent().getDefXMLinterne(nomSignalMem);
		Element elAwt = defAwt.getElement();
		out.println("\tpublic java.util.Vector<lilas.awt.MemoryDisplay<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()+">> "+varName+" = new java.util.Vector<lilas.awt.MemoryDisplay<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()+">>();");
	}
	
	public static void toJava(DefAWT defAwt, PrintWriter out, String varName) throws IOException {
		//Main.debug(""+varName+" "+defAwt.getName());
		String nomSignalMem = defAwt.getElement().getAttribute("memory");
		DefSignal def = (DefSignal) defAwt.getParent().getDefXMLinterne(nomSignalMem);
		Element elAwt = defAwt.getElement();
		String nameAwt = elAwt.getNodeName();
		String modèleAtt = elAwt.getAttribute("model");
		//out.print("\t\tlilas.awt.MemoryDisplay<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()+"> "+varName+" = new lilas.awt.MemoryDisplay<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()
		//		+">((lilas.base.Signal<"+def.getClassType().getName()+">) getSignal(\""+elAwt.getAttribute("memory")+
		//		"\"), ");
		out.print("\t\tlilas.awt.MemoryDisplay<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()+"> "+varName+" = new lilas.awt.MemoryDisplay<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()
				+">((lilas.base.Signal<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()+">) getSignal(\""+elAwt.getAttribute("memory")+
				"\"), ");
		if (modèleAtt.length()==0) out.print("this");
		else out.print("new "+modèleAtt+"((lilas.base.Signal<"+getClassTypeMemoryDisplayable(def.getClassType()).getName()+">) getSignal(\""+elAwt.getAttribute("memory")+"\")), this");

		if (elAwt.hasAttribute("sizeCol0")) out.print(", "+elAwt.getAttribute("sizeCol0")+", "+elAwt.getAttribute("sizeCol1"));
		else out.print(",0 , 0");
		if (elAwt.hasAttribute("height")) {
			if (elAwt.hasAttribute("width")) out.print(", "+elAwt.getAttribute("width"));
			else out.print(", "+elAwt.getAttribute("sizeCol0")+"+"+elAwt.getAttribute("sizeCol1"));
			out.println(", "+elAwt.getAttribute("height")+");");
		} else out.println(");");
		//out.println("\t\t"+varName+".add("+varName+");");
		// génération de la sensibilité pour l'interface awt
		//out.println("\t\t"+varName+"_sensibility = new lilas.base.Sensibility<java.lang.Long>(this, mem.getSignalDateModifExterne());");
		//out.println("\t\tmem.getSignalDateModifExterne().addSensibility("+varName+"_sensibility);");
	}
	
	
	public static void checkLilas(DefAWT defAwt, VectorError lstMsgErr) {
		//Main.debug("check MemoryDisplay "+defAwt);
		if (!defAwt.getElement().hasAttribute("memory"))
			lstMsgErr.add("Erreur sur l'élément MemoryDisplay : il manque l'attribut 'memory'", defAwt);
		else {
			String nomSignalMem = defAwt.getElement().getAttribute("memory");
			//Main.debug("check MemoryDisplay nomSignalMem="+nomSignalMem);
			DefXML def = defAwt.getParent().getDefXMLinterne(nomSignalMem);
			//Main.debug("check MemoryDisplay def="+def);
			if (def==null) {
				lstMsgErr.add("Element MemoryDisplay "+defAwt.getName()+" avec attribut memory='"+nomSignalMem+"' non défini dans le module parent", defAwt);
			} else if (!(def instanceof DefSignal)) {
				lstMsgErr.add("Element MemoryDisplay "+defAwt.getName()+" contenant un attribut signal='"+nomSignalMem+"' qui ne correspond pas à un signal défini dans le module parent!", defAwt);
			} else {
				Class classType = ((DefSignal)def).getClassType();
				if (classType==null || !isMemoryDisplayable(((DefSignal) def).getClassType()))
					lstMsgErr.add("Erreur sur l'élément MemoryDisplay "+defAwt.getName()+" : l'attribut 'memory' doit faire référence à un signal de MemoryDisplayable !", defAwt);
			}
		}
	}

	private static boolean isMemoryDisplayable(Class classe) {
		Class[] interfaces = classe.getInterfaces();
		//Main.debug(""+classe+" "+interfaces);
		for (int i=0; i<interfaces.length;i++) {
			//Main.debug(" "+interfaces[i]);
			if (interfaces[i]==MemoryDisplayable.class) return true;
		}
		if (classe!=Object.class) return isMemoryDisplayable(classe.getSuperclass());
		else return false;
	}
	
	private static Class getClassTypeMemoryDisplayable(Class classe) {
		Class[] interfaces = classe.getInterfaces();
		for (int i=0; i<interfaces.length;i++) {
			if (interfaces[i]==MemoryDisplayable.class) return classe;
		}
		if (classe!=Object.class) return getClassTypeMemoryDisplayable(classe.getSuperclass());
		else return null;
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		repaint();
		jtable.tableChanged(e);
	}

	public static void addDéclarationsComplémentaires(DefAWT defAwt, Hashtable<String, String> table) {
		table.put("mem", "lilas.base.Memory");
	}

}
