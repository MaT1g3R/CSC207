package warehouse;

import java.util.ArrayList;

/**
 * A class to simulate real world events from an input file.
 *
 * @author Peijun
 */
public class Simulator {

  private Warehouse warehouse;
  private ArrayList<String> eventList;

  public Simulator(String eventFile) {
    this.eventList = CsvReadWrite.readCsv(eventFile);
  }

  public void outPutResult(String outPath) {

  }

  public void run() {
  }

}
