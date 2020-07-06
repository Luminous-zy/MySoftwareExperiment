package team.zw.lifegame;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CellTest {
	private static Cell cell = new Cell(26, 16);
	private static int [][]status = new int[28][18];
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}


	@Test
	public void testGetStatus() {
		for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++)
            	status[i][j] = 0;
        }
		
		status[22][7] = 1;
		status[19][8] = 1;
		status[12][12] = 1;
		
		cell.setStatus(status);
		status = cell.getStatus();
		
		assertEquals(1,status[19][8]);
		assertEquals(1,status[22][7]);
		assertEquals(0,status[3][4]);
		
	}


	@Test
	public void testUpdate() {
		for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++)
            	status[i][j] = 0;
        }
		
		status[11][7] = 1;
		status[10][8] = 1;
		status[12][8] = 1;
		status[25][7] = 1;
		status[25][8] = 1;
		status[24][8] = 1;
		
		cell.setStatus(status);
		cell.update();
		status = cell.getStatus();

		assertEquals(1,status[11][8]);
		assertEquals(1,status[24][7]);
		assertEquals(1,status[25][8]);
		assertEquals(0,status[2][2]);
	}

	
	@Test
	public void testGetNeighborCount() {
		for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++)
            	status[i][j] = 0;
        }
		
	    status[10][4] = 1;
	    status[12][4] = 1;
	    status[10][6] = 1;	
	    cell.setStatus(status);
		assertEquals(3,cell.getNeighborCount(11,5));
		
		status[22][3] = 1;
		status[22][4] = 1;
		status[22][5] = 1;
		status[24][3] = 1;
		status[24][4] = 1;
		status[24][5] = 1;
		status[23][3] = 1;
		status[23][5] = 1;
		cell.setStatus(status);
		assertEquals(8,cell.getNeighborCount(23,4));
		
		status[4][15] = 1;status[6][16] = 1;
		cell.setStatus(status);
		assertEquals(2,cell.getNeighborCount(5,16));	
		
		status[21][9] = 1;
		status[21][10] = 1;
		status[21][11] = 1;
		status[19][9] = 1;
		status[19][11] = 1;
		cell.setStatus(status);
		assertEquals(5,cell.getNeighborCount(20,10));
					
	}

}

