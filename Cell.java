import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;


public class Cell extends JButton {
    hexsweeper model = new hexsweeper();
        private final int row;
        private final int col;
        private       int value;

       Cell(final int row, final int col) {
            this.row = row;
            this.col = col;
            setText("");
        }

        int getValue() {
            return value;
        }
        
        int getRow() {
            return row;
        }

        void setValue(int value) {
            this.value = value;
        }

        boolean isAMine() {
            return value == model.MINE;
        }

        void reset() {
            setValue(0);
            setEnabled(true);
            setText("");
            model.mineNum = 0;
        }

        void reveal() {
            setEnabled(false);
            setText(isAMine() ? "X" : String.valueOf(value));
        }

        void updateNeighbourCount() {
            getNeighbours(model.reusableStorage);
            for (Cell neighbour : model.reusableStorage) {
                if (neighbour == null) {
                    break;
                }
                if (neighbour.isAMine()) {
                    value++;
                }
            }
        }

        void getNeighbours(final Cell[] container) {
            for (int i = 0; i < model.reusableStorage.length; i++) {
                model.reusableStorage[i] = null;
            }

            int index = 0;

            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    if (rowOffset == 0 && colOffset == 0) {
                        continue;
                    }
                    int rowValue = row + rowOffset;
                    int colValue = col + colOffset;

                    if (rowValue < 0 || rowValue >= model.gridSize
                        || colValue < 0 || colValue >= model.gridSize) {
                        continue;
                    }

                    container[index++] = model.cells[rowValue][colValue];
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Cell cell = (Cell) obj;
            return row == cell.row &&
                   col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }