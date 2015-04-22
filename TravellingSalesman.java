import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TravellingSalesman {
	static final int NUM_OF_TOWNS = 100;
	static final int MAYNOOTH = 58; //Value should = 58
	static double[][] matrix = new double[NUM_OF_TOWNS][NUM_OF_TOWNS];
	static Town[] towns = new Town[NUM_OF_TOWNS];
	//static int path[];

	public static void main(String args[]) {
		System.out.println("START");
		long startTime = System.currentTimeMillis();
		readFile(new File("Friday.txt"));
		buildDistanceAdjacencyMatrix();

		int shortest[] = shortestPathNext(MAYNOOTH);

		int newShortest[];
		for (int i = 0; i < NUM_OF_TOWNS; i++) {
			newShortest = shortestPathNext(i);
			newShortest = TwoOptT(newShortest);
			if (getTotalDistance(newShortest) < getTotalDistance(shortest))
				shortest = copyArray(newShortest);
		}


		shortest = townSwaps(shortest);
		printOutPath(shortest);
		System.out.println(getTotalDistance(shortest));

//		shortest = TwoOptT(shortest);
//		printOutPath(shortest);
//		System.out.println(getTotalDistance(shortest));


		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (double) ((endTime - startTime) / 1000) + " seconds");
		System.out.println("It took " + (double) (((endTime - startTime) / 1000) / 60) + " minutes");
	}

	public static int[] townSwaps(int shortest[]) { //swap between 3 nearest cities

		int swaps[] = copyArray(shortest);
		restart: {
			for (int s = 1; s < NUM_OF_TOWNS - 2; s++) {
				int i = 0;
				do {

					//					System.out.println("s+i = " + (s+i));
					//					System.out.println("(s+(i + 1))%3 = " + (s+(i + 1))%3);
					swapTowns(swaps, (s + i), s + ((i + 1) % 3));

					double swapsdistance = getTotalDistance(swaps);
					double bestpathdist = getTotalDistance(shortest);
					if (swapsdistance < bestpathdist) {
						shortest = copyArray(swaps); // Save shorter path
						// We found a better path restart algo
						break restart;
					}
					swaps = copyArray(shortest); // reset array back to original
					i++;
				} while ((i != 3));
			}
		}
		return shortest;
	}

	public static int[] TwoOptT(int path[]) {
		int TwoOptPath[] = new int[NUM_OF_TOWNS + 1];
		for (int i = 0; i < path.length; i++) {
			TwoOptPath[i] = path[i];
			TwoOptPath[path.length] = TwoOptPath[0];
		}
		path = new int[NUM_OF_TOWNS + 1];
		path = copyArray(TwoOptPath);

		boolean improvement;
		do {
			restart: {
				improvement = false;

				TwoOptPath = copyArray(path); // TwoOpthPath[i] = path[i]
				for (int start = 1; start < TwoOptPath.length; start++) {
					for (int end = TwoOptPath.length - 2; end > start; end--) {

						TwoOptPath = copyArray(path); // TwoOpthPath[i] = path[i]

						// Reverse Path
						int s = start;
						int e = end;
						while (s <= e) {
							//Swap start and end towns
							swapTowns(TwoOptPath, s++, e--);
						}

						double TwoOptdistance = getTotalDistance(TwoOptPath);
						double bestpathdist = getTotalDistance(path);
						if (TwoOptdistance < bestpathdist) {
							path = copyArray(TwoOptPath); // Save shorter path
							improvement = true;
							//break; //restart;		//Actually get better score with this in
						}
					}
				}
			}
		} while (improvement == true);
		int ret_array[] = new int[TwoOptPath.length - 1];
		for (int i = 0; i < TwoOptPath.length - 1; i++)
			ret_array[i] = TwoOptPath[i];
		return ret_array;
	}

	public static void swapTowns(int[] TwoOptPath, int s, int e) {
		int temp = TwoOptPath[s];
		TwoOptPath[s] = TwoOptPath[e];
		TwoOptPath[e] = temp;
	}

	public static int[] TwoOpt(int path[]) {
		int TwoOptPath[] = new int[NUM_OF_TOWNS];
		boolean improvement;
		do {
			restart: {
				improvement = false;

				TwoOptPath = copyArray(path); // TwoOpthPath[i] = path[i]
				for (int start = 1; start < NUM_OF_TOWNS; start++) {
					for (int end = NUM_OF_TOWNS - 1; end > start; end--) {

						TwoOptPath = copyArray(path); // TwoOpthPath[i] = path[i]

						// Reverse Path
						int s = start;
						int e = end;
						while (s <= e) {
							swapTowns(TwoOptPath, s++, e--);
						}

						double TwoOptdistance = getTotalDistance(TwoOptPath);
						double bestpathdist = getTotalDistance(path);
						if (TwoOptdistance < bestpathdist) {
							path = copyArray(TwoOptPath); // Save shorter path
							improvement = true;
							break restart;
						}
					}
				}
			}
		} while (improvement == true);
		return TwoOptPath;
	}

	public static int[] copyArray(int[] src) {
		int dest[] = new int[src.length];
		for (int i = 0; i < src.length; i++) {
			dest[i] = src[i];
		}
		return dest;
	}

	public static double getTotalDistance(int[] array) {
		double fitness = 0;
		for (int i = 0; i < NUM_OF_TOWNS; i++) {
			fitness += matrix[array[i]][array[(i + 1) % NUM_OF_TOWNS]];
		}
		return fitness;
	}

	public static void printOutPath(int path[]) {
		for (int i = 0; i < NUM_OF_TOWNS; i++){
			if(i!=NUM_OF_TOWNS-1)
				System.out.print((path[i] + 1) + ".");
			else
				System.out.print((path[i] + 1));
		}
		System.out.println();

	}

	public static int[] shortestPathNext(int currentcity) {
		double totaldistance = 0.0;
		int citiesvisited = 1, nearestcity = 0;
		boolean[] used = new boolean[NUM_OF_TOWNS];
		for (int i = 0; i < NUM_OF_TOWNS; i++) {
			used[i] = false;
		}

		used[currentcity] = true;	// was MAYNOOTH

		int shortest[] = new int[NUM_OF_TOWNS];
		int srt_index = 0;
		shortest[srt_index++] = currentcity;	// was maynooth got 2318

		while (citiesvisited < NUM_OF_TOWNS) {
			for (int i = 0; i < NUM_OF_TOWNS; i++) {
				if (!used[i] && matrix[currentcity][i] < matrix[currentcity][nearestcity]) {
					nearestcity = i;
				}
			}

			totaldistance = totaldistance + matrix[currentcity][nearestcity];
			used[nearestcity] = true;
			shortest[srt_index++] = nearestcity;
			currentcity = nearestcity;
			citiesvisited++;
			for (int i = 0; i < NUM_OF_TOWNS; i++) {
				if (!used[i]) {
					nearestcity = i;
				}
			}
		}
		return shortest;

	}

	public static void getDistance() {
		Scanner input = new Scanner(System.in);
		int town1, town2;
		while (true) {
			town1 = input.nextInt();
			town2 = input.nextInt();
			if (town1 == -1 || town2 == -1)
				break;
			System.out.println(towns[town1].ID + " ---> " + towns[town2].ID);
			System.out.println(matrix[town1][town2]);
		}
		input.close();
	}

	public static void buildDistanceAdjacencyMatrix() { // So 0-->3 is really town 1 to 4
		for (int i = 0; i < NUM_OF_TOWNS; i++) {
			for (int j = 0; j < NUM_OF_TOWNS; j++) {
				matrix[i][j] = towns[i].distance(towns[j]);
			}
		}
	}

	public static void readFile(File aFile) {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(aFile));
			String line = null;

			int i = 0;
			while ((line = input.readLine()) != null) {
				String params[] = line.split("\\s\\s+");
				towns[i] = new Town(Integer.valueOf(params[0]), params[1], Double.valueOf(params[2]), Double.valueOf(params[3]));
				i++;
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (input != null) {

					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
