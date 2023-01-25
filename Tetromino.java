
public class Tetromino {

    enum Shape {
        I, O, T, J, L, S, Z
    }

    private char[][] body;
    private Shape shape;
    private int col;
    private int row;
    private String type;
    public Tetromino ()
    {}
    public Tetromino(Shape s) {
        this.shape = s;
        switch (s) {
            case O:
                body = new char[][] { { 'O', 'O' }, { 'O', 'O' } };
                type = "O";
                break;
            case I:
                body = new char[][] { { 'I', 'I', 'I', 'I' } };
                type = "I";

                break;
            case J:
                body = new char[][] { { 'J', ' ', ' ' }, { 'J', 'J', 'J' } };
                type = "J";

                break;
            case T:
                body = new char[][] { { ' ', 'T', ' ' }, { 'T', 'T', 'T' } };
                type = "T";

                break;
            case Z:
                body = new char[][] { { 'Z', 'Z', ' ' }, { ' ', 'Z', 'Z' } };
                type = "Z";

                break;
            case L:
                body = new char[][] { { ' ', ' ', 'L' }, { 'L', 'L', 'L' } };
                type = "L";

                break;
            case S:
                body = new char[][] { { ' ', 'S', 'S' }, { 'S', 'S', ' ' } };
                type = "S";

                break;

        }
        row = body.length;
        col = body[0].length;
    }

    public String getType() {
        return type;
    }
    public Shape getShape()
    {
        return shape;
    }
    public char[][] getBody() {
        return body;
    }

    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void printTetromino() {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                System.out.print(body[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void rotate(char dir) {
        int size1 = getRow();
        int size2 = getCol();
        char[][] temp = new char[size2][size1];
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                if (dir == 'r' || dir == 'R') {
                    temp[j][size1 - i - 1] = body[i][j];
                } else if (dir == 'l' || dir == 'L') {
                    temp[size2 - j - 1][i] = body[i][j];
                }
            }
        }
        body = temp;

        setRow(size2);
        setCol(size1);
    }
}