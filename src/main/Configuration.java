 package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.commons.lang3.ArrayUtils;
import main.iPatPanel.CustomOutputStream;
import net.miginfocom.swing.MigLayout;


public class Configuration extends JFrame implements ActionListener, WindowListener{
	String  P_name = "NULL", G_name = "NULL", GD_name = "NULL", GM_name = "NULL",
			C_name = "", K_name = "",
			VCF_name = "", PED_name = "", MAP_name = "",
			BED_name = "", FAM_name = "", BIM_name = "";
	int C_provided = 0, K_provided = 0;
    int MOindex = 0;
    String file_format = "";
	///////////////////////////////////////////////////////////////////////////////////////
	Preferences pref;
	static Runtime[] runtime = new Runtime[iPatPanel.MOMAX];
	static Process[] process = new Process[iPatPanel.MOMAX];
	PrintStream printStream;
	///////////////////////////////////////////////////////////////////////////////////////
	//Config gapit
	JPanel main_panel;
	JButton go_gapit = new JButton("GO");
	
	JPanel wd_panel;
	Group_Value Project_g = new Group_Value("Task name");
	Group_Path WD_g = new Group_Path("Output Directory");
	JLabel format_g = new JLabel("");
	
	ListPanel panel_phenotype_g;
	String[] rowP_g;
	
	JPanel panel_co;
	Group_Value PCA = new Group_Value("PCA.total");
	Group_CheckBox N_Inher_g = new Group_CheckBox("Inheritable covariate");
	
	JPanel panel_filter_g;
	Group_CheckBox chr_g = new Group_CheckBox("By Chromosome");
	Group_Combo ms_g = new Group_Combo("By missing rate",
			new String[]{"No threshold", "20%", "10%", "5%"});
	Group_Combo maf_g = new Group_Combo("By MAF",
			new String[]{"No threshold", "5%", "10%", "20%"});
	
	JPanel panel_CMLM;
	Group_Combo K_cluster = new Group_Combo("Cluster", 
			new String[]{"average", "complete", "ward", "single", "mcquitty", "median", "centroid"});
	Group_Combo K_group = new Group_Combo("Group", 
			new String[]{"Mean", "Max", "Min", "Median"});
	Group_Combo model_select = new Group_Combo("Select a model",
			new String[]{"GLM", "MLM", "CMLM"});
	
	JPanel panel_advance;
	Group_Combo snp_frac = new Group_Combo("SNP fraction",
			new String[]{"1", "0.8", "0.5", "0.3", "0.1"});
	Group_Combo file_frag = new Group_Combo("File fragment",
			new String[]{"512", "256", "128", "64"});
	JCheckBox model_selection_s = new JCheckBox("Model selection");
	///////////////////////////////////////////////////////////////////////////////////////
	//Config farm
	JPanel main_panel_farm;
	JButton go_farm= new JButton("GO");

	JPanel wd_panel_farm;
	Group_Value Project_f = new Group_Value("Task name");
	Group_Path WD_f = new Group_Path("Output Directory");
	JLabel format_f = new JLabel("");
	
	ListPanel panel_phenotype_f;
	String[] rowP_f;
	
	JPanel panel_co_f;
	Group_CheckBox N_Inher_f = new Group_CheckBox("Inheritable covariate");
	
	JPanel panel_filter_f;
	Group_CheckBox chr_f = new Group_CheckBox("By Chromosome");
	Group_Combo ms_f = new Group_Combo("By missing rate",
			new String[]{"No threshold", "20%", "10%", "5%"});
	Group_Combo maf_f = new Group_Combo("By MAF",
			new String[]{"No threshold", "5%", "10%", "20%"});
	
	JPanel panel_adv_farm;
	Group_Combo method_bin = new Group_Combo("Method bin", 
			new String[]{"static", "optimum"});
	Group_Value maxloop = new Group_Value("Max Loop");
	///////////////////////////////////////////////////////////////////////////////////////	
	//Config plink
	JPanel main_panel_p;
	JButton go_p = new JButton("GO");
	
	JPanel panel_wd_p;
	Group_Value Project_p = new Group_Value("Task name");
	Group_Path WD_p = new Group_Path("Output Directory");
	JLabel format_p = new JLabel("");
 
	JPanel panel_co_p;
	Group_CheckBox N_Inher_p = new Group_CheckBox("Inheritable covariate");	
	
	JPanel panel_filter_p;
	Group_CheckBox chr_p = new Group_CheckBox("By Chromosome");
	Group_Combo ms_p = new Group_Combo("By missing rate",
			new String[]{"No threshold", "20%", "10%", "5%"});
	Group_Combo maf_p = new Group_Combo("By MAF",
			new String[]{"No threshold", "5%", "10%", "20%"}); 
	Group_CheckBox trt_p = new Group_CheckBox("By Trait");
	
	JPanel panel_analysis_p;
	Group_Combo ci_p = new Group_Combo("C.I.",
			new String[]{"95%", "97.5%", "99.5%"}); 
	///////////////////////////////////////////////////////////////////////////////////////	
	//Config rrBLUP
	JPanel main_panel_r;
	JButton go_r = new JButton("GO");
		
	JPanel wd_panel_r;
	Group_Value Project_r = new Group_Value("Task name");
	Group_Path WD_r = new Group_Path("Output Directory");
	JLabel format_r = new JLabel("");
		
	ListPanel panel_phenotype_r;
	String[] rowP_r;	
	
	///////////////////////////////////////////////////////////////////////////////////////	
	//Config convert
	JPanel main_panel_c;
	JPanel panel_wd_c;
	ListPanel panel_select;
	Group_Value Project_c = new Group_Value("Task name");
	Group_Path WD_c = new Group_Path("Output Directory");
	
	///////////////////////////////////////////////////////////////////////////////////////	
	int test_run = 0;
	///////////////////////////////////////////////////////////////////////////////////////
    String folder_path = new String();
	///////////////////////////////////////////////////////////////////////////////////////
	public Configuration(int MOindex, iPatPanel.FORMAT format, Findex[] file_index, int C, int K) throws FileNotFoundException, IOException{	
		this.MOindex = MOindex;	
		this.C_provided = C;
		this.K_provided = K;
		
		C_name = iPatPanel.TBfile[C_provided];
		K_name = iPatPanel.TBfile[K_provided];
		for(int i = 0; i < iPatPanel.maxfile; i++){
			switch(file_index[i].file){
				case P: 	P_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case G:		G_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case GD:	GD_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case GM:	GM_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case VCF:	VCF_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case PED:	PED_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case MAP:	MAP_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case BED:	BED_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case FAM:	FAM_name = iPatPanel.TBfile[file_index[i].tb]; break;
				case BIM:	BIM_name = iPatPanel.TBfile[file_index[i].tb]; break;
			}
		}
		JScrollPane pane_gapit = null;
		JScrollPane pane_farm = null;
		JScrollPane pane_plink = null;
		JScrollPane pane_rrblup = null;
		JScrollPane pane_convert= null;
		
		pref = Preferences.userRoot().node("/ipat"); 
		switch(format){
			case Hapmap:
				pane_gapit = config_gapit();
				pane_farm = config_farm();
				pane_plink = config_plink();
				pane_rrblup = config_rrblup();
				file_format = "Hapmap";
				break;
			case Numeric:
				pane_gapit = config_gapit();
				pane_farm = config_farm();
				pane_plink = config_plink();
				pane_rrblup = config_rrblup();
				file_format = "Numeric";
				break;
			case VCF:
				file_format = "VCF";
				break;
			case PLink_ASCII:
				file_format = "PLink_ASCII";
				break;
			case PLink_Binary:
				pane_gapit = config_gapit();
				pane_farm = config_farm();
				pane_plink = config_plink();
				pane_rrblup = config_rrblup();
				file_format = "PLink_Binary";
				break;
		}	
        	JTabbedPane mainPane = new JTabbedPane();
        	mainPane.addTab("GAPIT", pane_gapit);
        	mainPane.addTab("FarmCPU", pane_farm);
        	mainPane.addTab("PLINK", pane_plink);
        	mainPane.addTab("rrBLUP", pane_rrblup);
		this.setContentPane(mainPane);
		this.setTitle("Configuration");
		this.pack();
		this.show();	
		load();
		addWindowListener(this);
	}	
	
	public JScrollPane config_gapit() throws IOException{
		go_gapit.setFont(new Font("Ariashowpril", Font.BOLD, 40));		
		///////////////////////////////////////////////////////////////////////////////////////
		wd_panel = new JPanel(new MigLayout("fillx", "[][grow]"));
		wd_panel.add(Project_g.name, "cell 0 0 2 1");
		wd_panel.add(Project_g.longfield, "cell 0 1 2 1");
		wd_panel.add(WD_g.name,  "cell 0 2 2 1");
		wd_panel.add(WD_g.field, "cell 0 3 1 1");
		wd_panel.add(WD_g.browse, "cell 1 3 1 1");
		Project_g.longfield.setText("Project "+ MOindex);
		wd_panel.add(format_g, "cell 0 4 2 1");
		format_g.setText("The format is "+iPatPanel.format);
		wd_panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Project", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_phenotype_g = new ListPanel("Traits", "Excluded");
		String text = iPatPanel.read_lines(P_name, 1)[0];
		rowP_g = text.split("\t");
		for(int i = 1; i < rowP_g.length ; i++){
			panel_phenotype_g.addElement(rowP_g[i]);
		}		
		panel_phenotype_g.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Phenotype", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));	
		///////////////////////////////////////////////////////////////////////////////////////
		panel_co = new JPanel(new MigLayout("fillx"));
		PCA.name.setToolTipText("Total Number of PCs as Covariates");
		if(C_provided != 0){	
			panel_co.add(N_Inher_g.check, "cell 0 0, align r");
			panel_co.add(N_Inher_g.field, "cell 1 0, align l");
		}else{
			panel_co.add(PCA.name, "cell 0 0, align r");
			panel_co.add(PCA.field, "cell 1 0 , align l");
		}
		panel_co.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Covariates", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_filter_g = new JPanel(new MigLayout("fillx"));
		panel_filter_g.add(chr_g.check, "cell 0 0, align r");
		panel_filter_g.add(chr_g.longfield, "cell 1 0,align l");
		panel_filter_g.add(ms_g.name, "cell 0 1, align r");
		panel_filter_g.add(ms_g.combo, "cell 1 1, align l");            
		panel_filter_g.add(maf_g.name, "cell 0 2, align r");
		panel_filter_g.add(maf_g.combo, "cell 1 2, align l");
		panel_filter_g.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filter", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_CMLM = new JPanel(new MigLayout("fillx"));
		K_cluster.name.setToolTipText("Clustering algorithm to group individuals based on their kinship");
		K_group.name.setToolTipText("Method to derive kinship among groups");

		panel_CMLM.add(model_select.name, "cell 0 0, align r");
		panel_CMLM.add(model_select.combo, "cell 1 0, align l");
		model_select.combo.setSelectedItem("CMLM");
		panel_CMLM.add(K_cluster.name, "cell 0 1, align r");
		panel_CMLM.add(K_cluster.combo, "cell 1 1, align l");
		panel_CMLM.add(K_group.name, "cell 0 2, align r");
		panel_CMLM.add(K_group.combo, "cell 1 2, align l");
	
		panel_CMLM.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Model", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_advance = new JPanel(new MigLayout("fillx"));
		
		snp_frac.name.setToolTipText("<html> The computations of kinship and PCs are extensive with large number of SNPs. <br>"
				+ "Sampling a fraction of it would reduce computing time. <br>"
				+ "The valid value sould be greater than 0 and no greater than 1 </html>");
		snp_frac.combo.setSelectedIndex(0);
		
		file_frag.name.setToolTipText("<html> With large amount of individuals, <br>"
				+ "loading a entire large genotype dataset could be difficult. <br>"
				+ "GAPIT can load a fragment of it each time. <br>"
				+ "The default of the fragment size is 512 SNPs </html>");
		file_frag.combo.setSelectedIndex(0);
		
		model_selection_s.setToolTipText("<html> GAPIT has the capability to conduct BIC-based model selection <br>"
				+ "to find the optimal number of PCs for inclusion in the GWAS models. </html>");
		model_selection_s.setSelected(false);
		
		panel_advance.add(snp_frac.name, "cell 0 0, align r");
		panel_advance.add(snp_frac.combo, "cell 1 0, align l");
		panel_advance.add(file_frag.name, "cell 0 1, align r");
		panel_advance.add(file_frag.combo, "cell 1 1, align l");
		panel_advance.add(model_selection_s, "cell 0 2 2 1, align c");
		panel_advance.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Advance", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		main_panel = new JPanel(new MigLayout("fillx", "[grow]"));
		main_panel.add(go_gapit, "dock north");
		main_panel.add(wd_panel, "cell 0 0, grow");
		main_panel.add(panel_phenotype_g, "cell 0 1, grow");
		main_panel.add(panel_co, "cell 0 2, grow");
		main_panel.add(panel_filter_g, "cell 0 3, grow");
		main_panel.add(panel_CMLM, "cell 0 4, grow");
		main_panel.add(panel_advance, "cell 0 5, grow");	
		///////////////////////////////////////////////////////////////////////////////////////
		JScrollPane pane = new JScrollPane(main_panel,  
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16); //scrolling sensitive
		pref = Preferences.userRoot().node("/ipat"); 
		go_gapit.addActionListener(this);
		WD_g.browse.addActionListener(this);
		return pane;
	}
	
	public JScrollPane config_farm() throws IOException{
		go_farm.setFont(new Font("Ariashowpril", Font.BOLD, 40));	
		wd_panel_farm = new JPanel(new MigLayout("fillx", "[][grow]"));
		wd_panel_farm.add(Project_f.name, "cell 0 0 2 1");
		wd_panel_farm.add(Project_f.longfield, "cell 0 1 2 1");
		wd_panel_farm.add(WD_f.name,  "cell 0 2 2 1");
		wd_panel_farm.add(WD_f.field, "cell 0 3 1 1");
		wd_panel_farm.add(WD_f.browse, "cell 1 3 1 1");
		Project_f.longfield.setText("Project "+ MOindex);
		wd_panel_farm.add(format_f, "cell 0 4 2 1");
		format_f.setText("The format is "+iPatPanel.format);
		wd_panel_farm.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Project", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_phenotype_f = new ListPanel("Traits", "Excluded");
		String text = iPatPanel.read_lines(P_name, 1)[0];
		rowP_f = text.split("\t");
		for(int i = 1; i < rowP_f.length ; i++){
				panel_phenotype_f.addElement(rowP_f[i]);
		}		
		panel_phenotype_f.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Phenotype", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));	
		///////////////////////////////////////////////////////////////////////////////////////
		panel_filter_f = new JPanel(new MigLayout("fillx"));
		panel_filter_f.add(chr_f.check, "cell 0 0, align r");
		panel_filter_f.add(chr_f.longfield, "cell 1 0,align l");
		panel_filter_f.add(ms_f.name, "cell 0 1, align r");
		panel_filter_f.add(ms_f.combo, "cell 1 1, align l");
		panel_filter_f.add(maf_f.name, "cell 0 2, align r");
		panel_filter_f.add(maf_f.combo, "cell 1 2, align l");
		panel_filter_f.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filter", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_co_f = new JPanel(new MigLayout("fillx"));
		panel_co_f.add(N_Inher_f.check, "cell 0 0, align r");
		panel_co_f.add(N_Inher_f.field, "cell 1 0, align l");
		panel_co_f.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Covariates", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_adv_farm = new JPanel(new MigLayout("fillx"));
		panel_adv_farm.add(method_bin.name,  "cell 0 0, align r");
		panel_adv_farm.add(method_bin.combo,  "cell 1 0, align l");
		panel_adv_farm.add(maxloop.name,  "cell 0 1, align r");
		panel_adv_farm.add(maxloop.field,  "cell 1 1, align l");;
		panel_adv_farm.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Advance", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		main_panel_farm = new JPanel(new MigLayout("fillx", "[grow]"));
		main_panel_farm.add(go_farm, "dock north");
		main_panel_farm.add(wd_panel_farm, "cell 0 0, grow");
		main_panel_farm.add(panel_phenotype_f, "cell 0 1, grow");
		if(C_provided != 0){
			main_panel_farm.add(panel_co_f, "cell 0 2, grow");
			main_panel_farm.add(panel_filter_f, "cell 0 3, grow");
			main_panel_farm.add(panel_adv_farm, "cell 0 4, grow");
		}else{
			main_panel_farm.add(panel_filter_f, "cell 0 2, grow");
			main_panel_farm.add(panel_adv_farm, "cell 0 3, grow");	
		}
		///////////////////////////////////////////////////////////////////////////////////////
		JScrollPane pane = new JScrollPane(main_panel_farm,  
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16); //scrolling sensitive
		go_farm.addActionListener(this);
		WD_f.browse.addActionListener(this);
		return pane;
	}
	
	public JScrollPane config_plink(){
		go_p.setFont(new Font("Ariashowpril", Font.BOLD, 40));
		panel_wd_p = new JPanel(new MigLayout("fillx", "[][grow]"));
		panel_wd_p.add(Project_p.name, "cell 0 0 2 1");
		panel_wd_p.add(Project_p.longfield, "cell 0 1 2 1");
		panel_wd_p.add(WD_p.name,  "cell 0 2 2 1");
		panel_wd_p.add(WD_p.field, "cell 0 3 1 1");
		panel_wd_p.add(WD_p.browse, "cell 1 3 1 1");
		Project_p.longfield.setText("Project "+ MOindex);
		panel_wd_p.add(format_p, "cell 0 4 2 1");
		format_p.setText("The format is "+iPatPanel.format);
		panel_wd_p.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Project", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_filter_p = new JPanel(new MigLayout("fillx"));
		panel_filter_p.add(chr_p.check, "cell 0 0, align r");
		panel_filter_p.add(chr_p.longfield, "cell 1 0,align l");
		panel_filter_p.add(ms_p.name, "cell 0 1, align r");
		panel_filter_p.add(ms_p.combo, "cell 1 1, align l");
		panel_filter_p.add(maf_p.name, "cell 0 2, align r");
		panel_filter_p.add(maf_p.combo, "cell 1 2, align l");
		panel_filter_p.add(trt_p.check, "cell 0 3, align r");
		panel_filter_p.add(trt_p.longfield, "cell 1 3, align l");
		panel_filter_p.add(ci_p.name, "cell 0 4, align r");
		panel_filter_p.add(ci_p.combo, "cell 1 4, align l");		
		panel_filter_p.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filter", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		main_panel_p = new JPanel(new MigLayout("fillx", "[grow]"));
		main_panel_p.add(go_p, "dock north");
		main_panel_p.add(panel_wd_p, "cell 0 0, grow");
		main_panel_p.add(panel_filter_p, "cell 0 1, grow");
		///////////////////////////////////////////////////////////////////////////////////////
		JScrollPane pane = new JScrollPane(main_panel_p,  
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16); //scrolling sensitive
		
		go_p.addActionListener(this);	
		WD_p.browse.addActionListener(this);
		return pane;
	}
	
	public JScrollPane config_rrblup() throws IOException{
		go_r.setFont(new Font("Ariashowpril", Font.BOLD, 40));	
		wd_panel_r = new JPanel(new MigLayout("fillx", "[][grow]"));
		wd_panel_r.add(Project_r.name, "cell 0 0 2 1");
		wd_panel_r.add(Project_r.longfield, "cell 0 1 2 1");
		wd_panel_r.add(WD_r.name,  "cell 0 2 2 1");
		wd_panel_r.add(WD_r.field, "cell 0 3 1 1");
		wd_panel_r.add(WD_r.browse, "cell 1 3 1 1");
		Project_r.longfield.setText("Project "+ MOindex);
		wd_panel_r.add(format_r, "cell 0 4 2 1");
		format_r.setText("The format is "+iPatPanel.format);
		wd_panel_r.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Project", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));
		///////////////////////////////////////////////////////////////////////////////////////
		panel_phenotype_r = new ListPanel("Traits", "Excluded");
		String text = iPatPanel.read_lines(P_name, 1)[0];
		rowP_r = text.split("\t");
		for(int i = 1; i < rowP_r.length ; i++){
			panel_phenotype_r.addElement(rowP_r[i]);
		}		
		panel_phenotype_r.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Phenotype", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));	
		///////////////////////////////////////////////////////////////////////////////////////
		main_panel_r = new JPanel(new MigLayout("fillx", "[grow]"));
		main_panel_r.add(go_r, "dock north");
		main_panel_r.add(wd_panel_r, "cell 0 0, grow");
		main_panel_r.add(panel_phenotype_r, "cell 0 1, grow");
		///////////////////////////////////////////////////////////////////////////////////////
		JScrollPane pane = new JScrollPane(main_panel_r,  
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16); //scrolling sensitive
		go_r.addActionListener(this);
		WD_r.browse.addActionListener(this);
		return pane;
	}
	
	public JScrollPane config_convert(){
		panel_select = new ListPanel("Traits", "");
		String[] items = {"one", "two", "three"};
		panel_select.addElements(items);
		JScrollPane pane = new JScrollPane(panel_select,  
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16); //scrolling sensitive		
		WD_c.browse.addActionListener(this);
		return pane;
	}

	@Override
	public void actionPerformed(ActionEvent ip){
	      	Object source = ip.getSource();	
	      	//GAPIT
	      	if (source == go_gapit){
	    	 	save();
	    	  	showConsole(MOindex, Project_g.longfield.getText(), WD_g.field.getText());	            
	    	  	String[] Command = run_GAPIT(iPatPanel.file_index);
	    	  	iPatPanel.multi_run[MOindex] = new BGThread(MOindex, Command, WD_g.field.getText(), Project_g.longfield.getText(), true, false, null, null);
	    	  	iPatPanel.multi_run[MOindex].start();
	    	  	this.dispose(); 	  		      
	      	}else if(source == WD_g.browse){
	    	  	WD_g.setPath(true);
	      	//Farm
	      	}else if(source == go_farm){
	    	  	save();
	    	  	showConsole(MOindex, Project_f.longfield.getText(), WD_f.field.getText());	            
	    	  	String[] Command = run_Farm(iPatPanel.file_index);
	    	  	iPatPanel.multi_run[MOindex] = new BGThread(MOindex, Command, WD_f.field.getText(), Project_f.longfield.getText(), true, false, null, null);
	    	  	iPatPanel.multi_run[MOindex].start();
	    	  	this.dispose(); 
	    	 }else if(source == WD_f.browse){
	    	  	WD_f.setPath(true);    
	      	//Plink
	      	}else if(source == go_p){
	    	  	save();
	    	  	showConsole(MOindex, Project_p.longfield.getText(), WD_p.field.getText());	            
	    	  	String[] Command = run_PLink(iPatPanel.file_index);
	    	  	System.out.println("Plink Begin");
	    	  	String[] plot_com = {"", iPatPanel.jar.getParent()+"/libs/PLinkPlots.R",
			  	         WD_p.field.getText(), Project_p.longfield.getText(), "3", iPatPanel.jar.getParent()+"/libs/"};
	    	  	iPatPanel.multi_run[MOindex] = new BGThread(MOindex, Command, WD_p.field.getText(), Project_p.longfield.getText(), false, true, plot_com, true);
	    	  	iPatPanel.multi_run[MOindex].start();    	
	    	  	this.dispose(); 	
	      	}else if(source == WD_p.browse){
	    	 	WD_p.setPath(true);    
	      	}else if(source == go_r){
	    	  	save();
	    	  	showConsole(MOindex, Project_r.longfield.getText(), WD_r.field.getText());	            
	    	  	String[] Command = run_rrBLUP(iPatPanel.file_index);
	    	  	iPatPanel.multi_run[MOindex] = new BGThread(MOindex, Command, WD_r.field.getText(), Project_r.longfield.getText(), true, false, null, null);
	    	  	iPatPanel.multi_run[MOindex].start();
	    	  	this.dispose(); 	
	      	}else if(source == WD_r.browse){
	    	  	WD_r.setPath(true);    
	      	}	
	}

	String[] run_GAPIT(Findex[] file_index){
		String model_selection_string = "";
		String 	K = "NULL", C = "NULL", C_inher = "NULL",
				SNP_test = "", PCA_count = "",
				ki_c = "", ki_g = "", 
				g_from = "", g_to = "", g_by = "", 
				SNP_fraction = "", file_fragment = "", WD = "", Project_name = "";
		////// Multiple trait
		String[] out = panel_phenotype_g.getElement(); //get remain traits
		String[] indexp = new String[out.length]; //create array for index
	
		for (int i = 0; i < out.length; i++){
			indexp[i] = Integer.toString(Arrays.asList(rowP_g).indexOf(out[i])); // get selected index
		}
		System.out.println("length"+indexp.length);
		//////
		SNP_test = "TRUE";
		K = K_provided != 0 ? K_name:K;
		C = C_provided != 0 ? C_name:C;
		PCA_count = C_provided != 0 ? "0":PCA.field.getText();
		C_inher = N_Inher_g.check.isSelected() ? N_Inher_g.field.getText():"NULL";
	
		switch((String)model_select.combo.getSelectedItem()){
		case "GLM":
			ki_c = (String) K_cluster.combo.getSelectedItem();
			ki_g = (String) K_group.combo.getSelectedItem();
			g_from = "1";
			g_to = "1";
			g_by = "10";
			break;
		case "CMLM":
			ki_c = (String) K_cluster.combo.getSelectedItem();
			ki_g = (String) K_group.combo.getSelectedItem();
			g_from = "1";
			g_to = "10000000";
			g_by = "10";
			break;
		case "MLM":
			ki_c = (String) K_cluster.combo.getSelectedItem();
			ki_g = (String) K_group.combo.getSelectedItem();
			g_from = "10000000";
			g_to = "10000000";
			g_by = "10";
			break;
		}	
		model_selection_string = model_selection_s.isSelected()?"TRUE":"FALSE";
		SNP_fraction = (String) snp_frac.combo.getSelectedItem();
		file_fragment = (String) file_frag.combo.getSelectedItem();
		
        System.out.println("running gapit"); 
        // Command input
        String[] command = {"", iPatPanel.jar.getParent()+"/libs/Gapit.R",
        		G_name, GM_name, GD_name, P_name, K, SNP_test, C, PCA_count, C_inher,
        		ki_c, ki_g, g_from, g_to, g_by, 
        		model_selection_string, SNP_fraction, file_fragment, WD_g.field.getText(), iPatPanel.jar.getParent()+"/libs/"};  
        String[] whole = (String[])ArrayUtils.addAll(command, indexp);
        return whole;
	}
	
	String[] run_Farm(Findex[] file_index){
		String 	C = "", C_inher = "", WD = "", Project_name = "",	
				method_b = "", maxloop_run = "", maf_cal = "", maf_threshold = "";		
		////// Multiple trait
		String[] out = panel_phenotype_f.getElement(); //get remain traits
		String[] indexp = new String[out.length]; //create array for index
		
		for (int i = 0; i < out.length; i++){
			indexp[i] = Integer.toString(Arrays.asList(rowP_f).indexOf(out[i])); // get selected index
		}
		System.out.println("length"+indexp.length);
		///	
		C = C_provided != 0 ? iPatPanel.TBfile[C_provided] : "NULL";
		C_inher = N_Inher_f.check.isSelected()? N_Inher_f.field.getText(): "NULL";

		method_b = (String) method_bin.combo.getSelectedItem();
		maxloop_run = maxloop.field.getText();		
		int maf_value = maf_f.combo.getSelectedIndex();
		switch(maf_value){
			case 0:
				maf_cal = "FALSE";
			case 1:
				maf_cal = "TRUE";
				maf_threshold = "0.05";
			case 2:
				maf_cal = "TRUE";
				maf_threshold = "0.1";
			case 3:
				maf_cal = "TRUE";
				maf_threshold = "0.2";
		}		
		// Format 
		GD_name = iPatPanel.format == iPatPanel.FORMAT.Hapmap ? G_name:GD_name;
        System.out.println("running FarmCPU");  
        String[] command = {"", iPatPanel.jar.getParent()+"/libs/FarmCPU.R",
        		GM_name, GD_name, P_name, C, C_inher,
        		method_b, maxloop_run, maf_cal, maf_threshold, WD_f.field.getText(), iPatPanel.jar.getParent()+"/libs/", file_format}; 
        String[] whole = (String[])ArrayUtils.addAll(command, indexp);
        return whole;
	}
	
	String[] run_PLink(Findex[] file_index){
		String 	WD = "", Project_name;
		String 	ci = "", ms = "", maf = "";
			
		WD = WD_p.field.getText();
		Project_name = Project_p.longfield.getText(); 
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(iPatPanel.jar.getParent()+"/libs/plink");
		list.add("--bed"); list.add(BED_name);
		list.add("--bim"); list.add(BIM_name);
		list.add("--fam"); list.add(FAM_name);
		list.add("--assoc"); list.add("--allow-no-sex"); 
		list.add("--pheno"); list.add(P_name);
		list.add("--all-pheno"); list.add("--adjust");
		switch(ci_p.combo.getSelectedIndex()){
			case 0: list.add("--ci"); list.add(".95"); break;
			case 1:	list.add("--ci"); list.add(".975"); break;
			case 2: list.add("--ci"); list.add(".995"); break;
		}
		switch(ms_p.combo.getSelectedIndex()){
			case 1:	list.add("--geno"); list.add(".2"); break;
			case 2: list.add("--geno"); list.add(".1"); break;
			case 3: list.add("--geno"); list.add(".05"); break;
		}
		switch(maf_p.combo.getSelectedIndex()){
			case 1:	list.add("--maf"); list.add(".05"); break;
			case 2: list.add("--maf"); list.add(".1"); break;
			case 3: list.add("--maf"); list.add(".2"); break;
		}
		list.add("--out"); list.add(WD+"/"+Project_name);
		String[] command = list.toArray(new String[0]);		
		//plink —bfile data —assoc —allow-no-sex —pheno phenos.txt —all-pheno —adjust —ci 0.95 —out output
		return command;
	}
	
	String[] run_rrBLUP(Findex[] file_index){
		String 	WD = "", Project_name = "";
		////// Multiple trait
		String[] out = panel_phenotype_r.getElement(); //get remain traits
		String[] indexp = new String[out.length]; //create array for index
		for (int i = 0; i < out.length; i++){
			indexp[i] = Integer.toString(Arrays.asList(rowP_r).indexOf(out[i])); // get selected index
		}
		
		System.out.println("running rrBLUP"); 
	    // Command input
	    String[] command = {"", iPatPanel.jar.getParent()+"/libs/rrBLUP.R",
	     					GD_name, P_name, WD_r.field.getText()};  
	    String[] whole = (String[])ArrayUtils.addAll(command, indexp);
	    return whole;
	}
	
	public void showConsole(int MOindex, String title, String MOPath){
		iPatPanel.MOname[MOindex].setText(title);
		iPatPanel.MOfile[MOindex] = MOPath;
		iPatPanel.text_console[MOindex] = new JTextArea();
        iPatPanel.text_console[MOindex].setEditable(false);
        iPatPanel.scroll_console[MOindex] = new JScrollPane(iPatPanel.text_console[MOindex] ,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        iPatPanel.frame_console[MOindex]  = new JFrame();
        iPatPanel.frame_console[MOindex].setContentPane(iPatPanel.scroll_console[MOindex]);
        iPatPanel.frame_console[MOindex].setTitle(title);
        iPatPanel.frame_console[MOindex].setSize(700,350);
        iPatPanel.frame_console[MOindex].setVisible(true); 
        iPatPanel.frame_console[MOindex].addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				if(process[MOindex].isAlive()){
					process[MOindex].destroy();
					iPatPanel.MO[MOindex] = iPatPanel.MO_fal;
				}
				System.out.println("Task killed");
			}
		});  
	}
	
	
	
	public void remove(){
		
	}
	
	public void load(){
		// GAPIT
		WD_g.field.setText(pref.get("WD_g", "~/"));
		model_select.combo.setSelectedIndex(pref.getInt("model_select_g", 0));
		PCA.field.setText(pref.get("PCA_g", "3"));
		N_Inher_g.field.setText(pref.get("C_inh_g", "0"));
		K_cluster.combo.setSelectedIndex(pref.getInt("K_cluster_g", 0));
		K_group.combo.setSelectedIndex(pref.getInt("K_group_g", 0));
		model_selection_s.setSelected(pref.getBoolean("model_selection_g", false));
		snp_frac.combo.setSelectedIndex(pref.getInt("snp_frac_g", 0));
		file_frag.combo.setSelectedIndex(pref.getInt("file_frag_g", 0));
		// FarmCPU
		WD_f.field.setText(pref.get("WD_f", "~/"));
		N_Inher_f.field.setText(pref.get("C_inh_f", "0"));
		method_bin.combo.setSelectedIndex(pref.getInt("method_bin_f", 0));
		maxloop.field.setText(pref.get("maxloop_f", "10"));
		maf_f.combo.setSelectedIndex(pref.getInt("maf_f", 0));
		// PLINK
		WD_p.field.setText(pref.get("WD_p", "~/"));
		System.out.println("LOAD");	
	}
	
	public void save(){
		// GAPIT
		pref.put("WD_g", WD_g.field.getText());
		//pref.put("Project_g", Project_g.longfield.getText());
		pref.putInt("model_select_g", model_select.combo.getSelectedIndex());
		pref.put("PCA_g", PCA.field.getText());
		pref.put("C_inh_g", N_Inher_g.field.getText());
		pref.putInt("K_cluster_g", K_cluster.combo.getSelectedIndex());
		pref.putInt("K_group_g", K_group.combo.getSelectedIndex());
		pref.putBoolean("model_selection_g", model_selection_s.isSelected());
		pref.putInt("snp_frac_g", snp_frac.combo.getSelectedIndex());
		pref.putInt("file_frag_g", file_frag.combo.getSelectedIndex());
		// FarmCPU
		pref.put("WD_f", WD_f.field.getText());
		pref.put("C_inh_f", N_Inher_f.field.getText());
		pref.putInt("method_bin_f", method_bin.combo.getSelectedIndex());
		pref.put("maxloop_f", maxloop.field.getText());
		pref.putInt("maf_f", maf_f.combo.getSelectedIndex());
		// PLINK
		pref.put("WD_p", WD_p.field.getText());
		
		System.out.println("SAVE");
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("close");	
		save();		
	}
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}	
}
