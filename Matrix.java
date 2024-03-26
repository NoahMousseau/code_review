import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Matrix {

	//Fields like 'm_rows', 'n_cols', and 'arr' are alterable, which could cause errors or the program to crash
	//Const could instead be used to declare these variables as constants.
	private int m_rows;
	private int n_cols;
	private double[][] arr;

	public Matrix(int m, int n) { //Validation should exist to make sure that the dimensions provided 
				      //during object instantiation are not negative numbers
		this.m_rows = m;
		this.n_cols = n;
		this.arr = new double[m][n];
		for (int i = 0; i < this.m_rows; i++) {
			for (int j = 0; j < this.n_cols; j++) {
				this.arr[i][j] = 0;
			}
		}
	}


	public Matrix(String filename) throws FileNotFoundException {
		Scanner inFile = new Scanner(new FileReader(filename));

		// Initialize array using first line of file -- size parameters
		String arrSize = inFile.nextLine(); //Handling of potential exceptions thrown by file operations, like if a file isn't where it should be, or
						    //if a file does not exist that should, should be included within the program
		StringTokenizer st = new StringTokenizer(arrSize, ",");
		this.m_rows = Integer.parseInt(st.nextToken());
		this.n_cols = Integer.parseInt(st.nextToken());

		this.arr = new double[m_rows][n_cols];


		int i = 0;
		String rowData;
		StringTokenizer st2;
		//Using the String.split() method might be better for splitting strings rather than the StringTokenizer method
		while (inFile.hasNextLine()) {
			// Fill array with data
			rowData = inFile.nextLine();
			st2 = new StringTokenizer(rowData, ",");
			for (int j = 0; j < this.n_cols; j++) {
				this.arr[i][j] = Double.parseDouble(st2.nextToken());
			}
			i++;
		}

	}

	//Add some kind of validation to the add() method to make sure that the matrix that is 
	//being added is within the correct size of a matrix that can be added
	public Matrix add(Matrix m) {// add 'this' matrix to matrix m
		Matrix newMat = new Matrix(this.m_rows, this.n_cols);
		for (int i = 0; i < this.m_rows; i++) {
			for (int j = 0; j < this.n_cols; j++) {
				newMat.arr[i][j] = this.arr[i][j] + m.arr[i][j];
			}
		}
		return newMat;
	}

	//Similarly, add some kind of validation to the subtract() method to make sure that the matrix that is being subtracted from
	//the instantiated method is within the correct size of a matrix that can be subtraced
	public Matrix subtract(Matrix m) {
		Matrix newMat = new Matrix(this.m_rows, this.n_cols);
		for (int i = 0; i < this.m_rows; i++) {
			for (int j = 0; j < this.n_cols; j++) {
				newMat.arr[i][j] = this.arr[i][j] - m.arr[i][j];
			}
		}
		return newMat;
	}

	//This method should be able to verify that the number of columns 
	//of the 1st (this) matrix should be equal to the number of rows of the 2nd (m) matrix, like:
	//col(this) == rows(m)
	public Matrix multiply(Matrix m) {
		Matrix newMat = new Matrix(this.m_rows, m.n_cols);// Create new matrix with correct dimensions

		if (this.n_cols == m.m_rows) {
			for (int row = 0; row < this.m_rows; row++) {
				for (int col = 0; col < m.n_cols; col++) {
					for (int k = 0; k < this.n_cols; k++)
						newMat.arr[row][col] += this.arr[row][k] * m.arr[k][col];
				}
			}
		} else {
			System.out.println("Arrays can't be multiplied");
			return newMat;
		}

		return newMat;
	}

	public Matrix multiply(double x) {
		Matrix newMat = new Matrix(this.m_rows, this.n_cols);
		for (int i = 0; i < this.m_rows; i++) {
			for (int j = 0; j < this.n_cols; j++) {
				newMat.arr[i][j] = this.arr[i][j] * x;
			}
		}
		return newMat;
	}
	
	public Matrix divide(double x) {
		//There should be a check in the divide() method to make sure the method 
		//cannot take a value of 0.0 double for the “double x” part of it
		double inverse=1/x;
		return multiply(inverse);
		
	}

	public double determinant() {
		//The program is limited to matrices of a 3x3 size, although it could be altered to take larger size matrices
		double result = -1;
		if (this.m_rows > 3) {
			System.out.println("Matrix too large -- Cannot find determinant");
			return -1;
		}
		if (this.m_rows == 1) {
			return this.arr[0][0];
		} else if (this.m_rows == 2) {
			result = (this.arr[0][0] * this.arr[1][1]) - (this.arr[1][0] * this.arr[0][1]);

		} else if (this.m_rows == 3) {
			result = (this.arr[0][0] * this.det_2x2(0)) - (this.arr[1][0] * this.det_2x2(1))
					+ (this.arr[2][0] * this.det_2x2(2));
		}
		//Try throwing an error from the program directly, instead of returning an error result
		return result;// error case
	}

	//This are method does not use camel-case, which should be used instead of fancier kinds of names for methods
	//determinant2x2(int rowX)
	private double det_2x2(int rowX) {

		if (rowX == 0)
			return (this.arr[1][1] * this.arr[2][2]) - (this.arr[2][1] * this.arr[1][2]);
		else if (rowX == 1)
			return (this.arr[0][1] * this.arr[2][2]) - (this.arr[2][1] * this.arr[0][2]);
		else if (rowX == 2)
			return (this.arr[0][1] * this.arr[1][2]) - (this.arr[1][1] * this.arr[0][2]);
		else
			return -1;// Error case

	}

	//This method could be used elsewhere within the class to perform validation and checks for other functions
	public boolean isSquare() {
		if (this.m_rows == this.n_cols)
			return true;
		else
			return false;
	}

	public Matrix transpose() {
		Matrix result = new Matrix(this.m_rows, this.n_cols);
		for (int i = 0; i < this.m_rows; i++) {
			for (int j = 0; j < this.n_cols; j++) {
				result.arr[i][j] = this.arr[j][i];
			}
		}
		return result;
	}

	public Matrix inverse() {
		//The inverse method is set at a size of taking only 3x3 matrices, 
		//it could be altered to handle larger size matrices, like 4x4 or 5x5
		Matrix newMat = new Matrix(this.m_rows, this.n_cols);
		if (this.isSquare()) {
			if (this.m_rows == 1) {
				newMat.arr[0][0] = 1 / this.arr[0][0];
			} else if (this.m_rows == 2) {
				double det = this.determinant();
				det = 1 / det;
				newMat.arr[0][0] = det * this.arr[1][1];
				newMat.arr[1][1] = det * this.arr[0][0];
				newMat.arr[0][1] = -1 * det * this.arr[0][1];
				newMat.arr[1][0] = -1 * det * this.arr[1][0];

			} else if (this.m_rows == 3) {
				double det = this.determinant();
				if (det == 0) {
					System.out.println("Determinant = 0, cannot find inverse");
					return newMat;
				}
				
				for(int i = 0; i < 3; ++i) 
					for( int j = 0; j < 3; ++j)
						newMat.arr[i][j]=(((arr[(j+1)%3][(i+1)%3] * arr[(j+2)%3][(i+2)%3]) - (arr[(j+1)%3][(i+2)%3] * arr[(j+2)%3][(i+1)%3]))/ det);
					
				
			} else {
				System.out.println("Problem encountered finding inverse");
				return newMat;
			}

		} else {
			//Error messages could be thrown directly by the program instead
			System.out.println("Cannot find inverse of non-square matrix");
			return newMat;
		}
		
		return newMat;
	}

	
	
	public int getM() {
		return this.m_rows;
	}

	public int getN() {
		return this.n_cols;
	}

	public double getElement(int i, int j) {
		return this.arr[i][j];
	}


	public String toString() {
		String result = "";
		result += this.m_rows + "," + this.n_cols + "\n";
		for (int i = 0; i < m_rows; i++) {
			for (int j = 0; j < n_cols; j++) {
				result += this.arr[i][j];
				if (j < n_cols - 1)
					result += ",";
				result += " ";
			}
			result += "\n";
		}
		return result;
	}

	public void print(String filename) throws IOException {
		String data = this.toString();
		FileWriter fw = new FileWriter(filename);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.flush();
		bw.close();
		//Packages and resources like FileWriter should also be closed after it 
		//has been finished being utilized within the code, not just the BufferedWriter
	}


	public static Matrix identity(int squareSize) {
		Matrix id = new Matrix(squareSize, squareSize);

		for (int i = 0; i < squareSize; i++) {
			for (int j = 0; i < squareSize; i++) {
				if (i == j)
					id.arr[i][j] = 1;
			}
		}
		return id;
	}


}
