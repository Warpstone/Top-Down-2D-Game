package game; // !!! Это должно совпадать с именем папки !!!

// --- Импорты для графики и окна ---
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame; 

// --- Импорты для обработки мыши ---
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

// Наследуем Canvas (для рисования), Runnable (для потока) и MouseListener (для кликов)
public class Game extends Canvas implements Runnable, MouseListener {
    
    // ⚙️ Настройки Окна
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final String TITLE = "Top-Down Shooter";

    // 🚩 Состояние игры
    private boolean running = false;
    private Thread thread;
    private STATE gameState = STATE.MENU; // Начинаем с меню

    // 🖼️ Конструктор - Настраиваем ОКНО
    public Game() {
        // Устанавливаем размер нашего холста (Canvas)
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        // Создаем Рамку (Окно) и добавляем наш холст
        JFrame frame = new JFrame(TITLE);
        frame.add(this); 
        
        // Настраиваем Рамку
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false);                            
        frame.pack(); 
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true); 

        // !!! ГОВОРИМ ХОЛСТУ, ЧТО МЫ БУДЕМ СЛУШАТЬ КЛИКИ МЫШИ !!!
        this.addMouseListener(this); 
    }

    // 🚀 Методы Start/Stop/Run (Игровой цикл)
    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this); 
        thread.start();
    }

    // ... (Метод stop() опущен для краткости, он есть в предыдущем коде)

    @Override
    public void run() {
        this.requestFocus(); // Фокусируемся на окне
        while (running) {
            update(); 
            render(); 
            try {
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 🔄 Обновление логики игры
    private void update() {
        // Логика обновления будет зависеть от состояния
        if (gameState == STATE.GAME) {
            // TODO: Обновляем игрока, врагов, пули
        }
    }

    // 🎨 Отрисовка
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3); 
            return;
        }

        Graphics g = bs.getDrawGraphics();
        
        // --- НАЧАЛО ОТРИСОВКИ ---
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (gameState == STATE.MENU) {
            renderMenu(g);
        } else if (gameState == STATE.GAME) {
            // TODO: Отрисовка игрового поля, игрока и т.д.
            g.setColor(Color.WHITE);
            g.drawString("ИГРА НАЧАЛАСЬ!", WIDTH / 2 - 50, HEIGHT / 2);
        }
        
        // --- КОНЕЦ ОТРИСОВКИ ---
        g.dispose(); 
        bs.show();   
    }

    // 🖼️ Отрисовка Меню
    private void renderMenu(Graphics g) {
        // Заголовок
        Font fontTitle = new Font("Arial", Font.BOLD, 72);
        g.setFont(fontTitle);
        g.setColor(Color.WHITE);
        g.drawString(TITLE, (WIDTH / 2) - 350, 150); 

        // Кнопки (Прямоугольники)
        Font fontButton = new Font("Arial", Font.PLAIN, 30);
        g.setFont(fontButton);
        g.setColor(Color.WHITE);
        
        // Кнопка "Начать Игру"
        g.drawRect((WIDTH / 2) - 150, 250, 300, 50); 
        g.drawString("Начать Игру", (WIDTH / 2) - 75, 285); 

        // Кнопка "Выход"
        g.drawRect((WIDTH / 2) - 150, 350, 300, 50); 
        g.drawString("Выход", (WIDTH / 2) - 40, 385); 
    }

    // 🖱️ ОБРАБОТКА МЫШИ (Самый важный кусок для меню!)
    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX(); // X-координата клика
        int my = e.getY(); // Y-координата клика
        
        if (gameState == STATE.MENU) {
            
            // 1. Проверяем попадание в кнопку "Начать Игру"
            // Кнопка: x=WIDTH/2 - 150 (362), y=250, ширина=300, высота=50
            if (mx >= (WIDTH / 2) - 150 && mx <= (WIDTH / 2) + 150) { // Проверка по X
                if (my >= 250 && my <= 300) { // Проверка по Y (250 + 50)
                    gameState = STATE.GAME; // Кликнули! Переключаемся в состояние ИГРА
                    System.out.println("Игра началась!");
                }
            }
            
            // 2. Проверяем попадание в кнопку "Выход"
            // Кнопка: x=WIDTH/2 - 150, y=350, ширина=300, высота=50
            if (mx >= (WIDTH / 2) - 150 && mx <= (WIDTH / 2) + 150) { 
                if (my >= 350 && my <= 400) { // Проверка по Y (350 + 50)
                    System.exit(1); // Кликнули! Закрываем программу
                }
            }
        }
    }

    // ⚠️ Обязательные, но пока не используемые методы MouseListener
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    // ------------------------------------------------------------------

    // 🚪 Главный метод - Точка входа
    public static void main(String[] args) {
        new Game().start(); // Создаем новый объект Game и запускаем!
    }
}