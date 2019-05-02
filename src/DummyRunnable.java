import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

	public class DummyRunnable implements Runnable {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String command;

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(((random.nextInt(3)) + 1) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 15; i++) {
                    sb.append((char) ('a' + random.nextInt(26)));
                }
                setCommand(sb.toString());
            }
        }

        public String getCommand() {
            return command;
        }

        private void setCommand(String command) {
            String old = this.command;
            this.command = command;
            pcs.firePropertyChange("command", old, command);
        }
    }
