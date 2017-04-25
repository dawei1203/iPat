<h1 style="text-align:center">User Manual for</h1>

<p align="center"><img src = "./res/icon.png" width = 200></p>
<h1 style="text-align:center">Intelligent Prediction and Association Tool</h1>

<h3 style="text-align:center">(Version 1.1)</h3>
<h3 style="text-align:center">Last updated on Apr 24, 2017</h3>

Normal Text

<p align="center">
  <b>Some Links:</b><br>
  <h3 style="text-align:center">(Version 1.1)</h3>
  <h3>(Version 1.1)</h3>

  <a href="#">Link 1</a> |
  <a href="#">Link 2</a> |
  <a href="#">Link 3</a>
  <br><br>
  <img src="http://s.4cdn.org/image/title/105.gif">
</p>

Normal text

# Table of Contents
#### 1. [Getting start](#get_start)
#### 2. [Interface](#interface)
> 2-1 [Import files](#import_files)

> 2-2 [Create a project](#create_projects)

> 2-3 [File formats](#file_format)

> 2-4 [Covariates and kinship](#C_K)

> 2-5 [Define input arguments](#input)

> 2-6 [Run an analysis](#run)

> 2-7 [Check the results](#check)

> 2-8 [Files remove](#delete)

#### 3. [GWAS and GS](#Gwas)

<br><br><br>

<a name="get_start"></a>
## 1. Getting start 
* Before launching iPat, remember to place folder 'libs' to the path where 'iPat.jar' exists. iPat can function normally only when it is in the same folder as ‘libs’.

	* Operation System: Mac OS X.
	* [Java Runtime Environment (JRE)](http://www.oracle.com/technetwork/java/javase/downloads/index.html): Version 8 or later.
	* [R](https://www.r-project.org): Version 3.4.0 or later. 


<center><img src = "./res/libs.png" width = 700></center>

<a name="interface"></a>
## 2. Interface

<a name="import_files"></a>
### 2.1 *Import files*


<center><img src = "./res/dnd.png" width = 700></center>

<a name="create_projects"></a>
### 2.2 *Create a project*
* After importing the files, double clicking on anywhere in iPat to create a new project.

<center><img src = "./res/linkages.png" width = 250></center>

<a name="file_format"></a>
### 2.3 *File formats*
* iPat can recognize and work fine with different formats, which include hapmap, numeric, vcf and plink.

* ***Imported file set need to have identical names and correct extension name if they need to be converted to a proper format.*** For example, if you want to perform GWAS using VCF format in FarmCPU, files set should be named as: data.vcf and data.txt. The table below shows examples of files and its extension name for the corresponded format:

<center>

|Format |Genotype|Phenotype|Other information|
|:-----:|:------:|:-------:|:---------------:|
|Hapmap |.hmp    |.txt     |None  	        |  
|Numeric|.dat    |.txt     |.map 			     |
|VCF    |.vcf    |.txt     |None             |
|PLINK  |.bed    |.txt     |.fam .bim        |
</center>



<a name="input"></a>
### 2.5 *Define input arguments*



<center><img src = "./res/config.png" width = 400></center>

<a name="run"></a>
* After defining the analysis, user can start to run the procedure by clicking ‘GO’ at the top of the panel.

<a name="check"></a>
### 2.7 *Check the result*
* When iPat complete a project, the gear icon will show a green dot if the task run successfully without any error occurred. Otherwise it will show a red dot at its top-left to notify users that there’re existing at least one error message during the analysis.

<center><img src = "./res/indicator.png" width = 700></center>
* Users can check the result by double clicking on the gear icon, which will directe users to the folder where the output files generated.

<center><img src = "./res/output.png" width = 700></center>

### 2.8 *Files remove*
* Users can remove objects and linkage by typing in “Del” after selecting 

* For linkages, the line will become solid when it’s selected. 

* For objects, there will be a dashed line surrounded to indicate that the object is selected.

* Users can also drag the linkages or objects to the bottom-right corner, a hidden trashcan will show up for deletion.

<center><img src = "./res/delete.png" width = 700></center>
