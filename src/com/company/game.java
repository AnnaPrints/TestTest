public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 2;
    private static final float SWIPE_VELOCITY = 2;
    private static boolean CAN_DRAW = false;
    private boolean GHOST_INICIALIZED=false;
    private GestureDetector gestureDetector;
    private GameManager gameManager;
    private Thread thread; //game thread
    private SurfaceHolder holder;
    private int blockSize;                // Ancho de la pantalla, ancho del bloque
    private static int movementFluencyLevel=8; //this movement should be a multiple of the blocksize and multiple of 4, if note the pacman will pass walls

    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado
    
   
    //----------------------------------------------------------------------------------------------
    //Constructors
    public GameView(Context context) {
        super(context);
        this.constructorHelper(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.constructorHelper(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.constructorHelper(context);


    }

    private void constructorHelper(Context context) {
        this.gestureDetector = new GestureDetector(this);
        setFocusable(true);
        this.holder = getHolder();
        this.holder.addCallback(this);
        this.frameTicker = (long) (1000.0f / totalFrame);

        this.gameManager=new GameManager();

        int screenWidth=getResources().getDisplayMetrics().widthPixels;
        this.blockSize = ((((screenWidth/this.gameManager.getGameMap().getMapWidth())/movementFluencyLevel)*movementFluencyLevel)/4)*4;
        this.holder.setFixedSize(blockSize*this.gameManager.getGameMap().getMapWidth(),blockSize*this.gameManager.getGameMap().getMapHeight());

        this.gameManager.getGameMap().loadBonusBitmaps(this.getBlockSize(),this.getResources(),this.getContext().getPackageName());
        this.gameManager.setPacman(new Pacman("pacman","",this.movementFluencyLevel,this.gameManager.getGameMap().getPacmanSpawnPosition(),this.blockSize,this.getResources(),this.getContext().getPackageName()));

        Ghost.loadCommonBitmaps(this.blockSize,this.getResources(),this.getContext().getPackageName());
    }
    //----------------------------------------------------------------------------------------------
    //Getters and setters
    public int getBlockSize() {
        return blockSize;
    }
    public GameManager getGameManager() {
        return gameManager;
    }
    public boolean isDrawing(){
        return CAN_DRAW;
    }
    //----------------------------------------------------------------------------------------------

    private synchronized void initGhost(){
        if(!GHOST_INICIALIZED){
            GHOST_INICIALIZED=true;
            this.gameManager.initGhosts(this.blockSize,this.getResources(),this.getContext().getPackageName(),movementFluencyLevel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        long gameTime;
        Canvas canvas;
        while (!holder.getSurface().isValid()) {
        }
        this.initGhost();
        this.setFocusable(true);
        while (CAN_DRAW) {
            gameTime=System.currentTimeMillis();
            if(gameTime > frameTicker + (totalFrame * 15)){
                canvas = holder.lockCanvas();
                if(canvas!=null){
                    if(this.updateFrame(gameTime,canvas)){
                        try {
                            Thread.sleep(3000);
                        }catch (Exception e){}
                    }
                    holder.unlockCanvasAndPost(canvas);
                    if(this.gameManager.checkWinLevel()){
                        CAN_DRAW=false;
                        this.gameManager.cancelThreads();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {}
                        //animation
                        Log.i("Game","You win");
                    }else if(!this.gameManager.getPacman().hasLifes()){
                        //we lost

                        CAN_DRAW=false;
                        this.gameManager.cancelThreads();

                        //animation
                        Log.i("Game","You lose");
                    }
                }
            }
        }

    }

    // Method to capture touchEvents
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //To swipe
        //https://www.youtube.com/watch?v=32rSs4tE-mc
        this.gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    //Chequea si se deberia actualizar el frame actual basado en el
    // tiempo que a transcurrido asi la animacion
    //no se ve muy rapida y mala
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean updateFrame(long gameTime, Canvas canvas) {
        Pacman pacman;
        Ghost[] ghosts;
        boolean pacmanIsDeath;

        pacman=this.gameManager.getPacman();
        ghosts=this.gameManager.getGhosts();

        // Si el tiempo suficiente a transcurrido, pasar al siguiente frame
        frameTicker = gameTime;
        canvas.drawColor(Color.BLACK);
        this.gameManager.getGameMap().draw(canvas, Color.BLUE,this.blockSize,this.gameManager.getLevel());
        this.gameManager.moveGhosts(canvas,this.blockSize);
        pacmanIsDeath=pacman.move(this.gameManager,canvas);

        if(!pacmanIsDeath){
            // incrementar el frame
            pacman.changeFrame();
            for(int i=0; i<ghosts.length;i++){
                ghosts[i].changeFrame();
            }
            currentArrowFrame++;
            currentArrowFrame%=7;
        }else{
            pacman.setNextDirection(' ');
            for(int i=0; i<ghosts.length;i++){
                ghosts[i].respawn();
            }
        }
        return pacmanIsDeath;
    }

    public int getScore(){
        return this.getGameManager().getScore();
    }

    public void setSemaphores(Semaphore changeScoreSemaphore, Semaphore changeDirectionSemaphore){
        this.gameManager.setChangeScoreSemaphore(changeScoreSemaphore);
        this.gameManager.getPacman().setChangeDirectionSemaphore(changeDirectionSemaphore);
        Log.i("Semaphore", "setted");
    }

    //----------------------------------------------------------------------------------------------
    //Callback methods
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CAN_DRAW = true;
        this.thread= new Thread(this);
        this.thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("Surface","destroyed");
    }

    //----------------------------------------------------------------------------------------------
    public void resume() {
        CAN_DRAW = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        CAN_DRAW = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // retry
            }
            break;
        }
        this.thread=null;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        //To swipe
        //https://www.youtube.com/watch?v=32rSs4tE-mc
        boolean result;
        float diffX, diffY;
        Pacman pacman;
        Log.i("Fling", "detected");

        result=false;
        diffX = moveEvent.getX() - downEvent.getX();
        diffY = moveEvent.getY() - downEvent.getY();
        pacman=this.gameManager.getPacman();

        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY){
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    //right
                    pacman.setNextDirection('r');
                } else {
                    //left
                    pacman.setNextDirection('l');
                }
            }else{
                if (diffY > 0) {
                    //down
                    pacman.setNextDirection('d');
                } else {
                    //up
                    pacman.setNextDirection('u');
                }
            }
            result=true;
        }
        return result;
    }

}


public class GameManager {
    private static final int TOTAL_LEVELS=256;
    private static int SCORE=0;
    private GameMap gameMap;
    private int level,bonusResetTime;//,score;
    private CountDownScareGhosts scareCountDown;
    private Pacman pacman;
    private Ghost[] ghosts;
    private boolean fruitHasBeenInTheLevel;
    private static Semaphore CHANGE_SCORE_MUTEX;

    public GameManager(){
        this.fruitHasBeenInTheLevel=false;
        //this.score=0;
        this.gameMap=new GameMap();
        this.gameMap.loadMap1();
        this.level=1;
        this.ghosts=new Ghost[4];
        this.bonusResetTime = 5000;
        this.scareCountDown=null;
    }

    public void setChangeScoreSemaphore(Semaphore changeScoreSemaphore) {
        CHANGE_SCORE_MUTEX = changeScoreSemaphore;
        //if(this.changeScoreSemaphore==null){
        //    Log.i("Change Score Semaphore","I'm null");
        //}else{
        //    Log.i("Change Score Semaphore","I'm not null");
        //}
    }

    public void addScore(int s){
        //this.score+=s;
        SCORE+=s;
        CHANGE_SCORE_MUTEX.release();
        /*if(this.changeScoreSemaphore==null){
            Log.i("Change Score Semaphore","I'm null");
        }else{
            Log.i("Change Score Semaphore","I'm not null");
        }*/
        //this.changeScoreSemaphore.release();
    }

    public int getScore() {
        return SCORE;
        //return this.score;
    }

    public int getLevel() {
        return this.level;
    }
    public GameMap getGameMap() {
        return this.gameMap;
    }
    public Ghost[] getGhosts(){
        return this.ghosts;
    }
    public Pacman getPacman(){
        return this.pacman;
    }
    public void setPacman(Pacman pacman){
        this.pacman=pacman;
    }


    public void eatPallet(int posXMap, int posYMap){
        SCORE+=10;
        CHANGE_SCORE_MUTEX.release();
        //this.score+=10;
        Log.i("Score GM", ""+SCORE);
        //Log.i("Score GM", ""+this.score);
        this.gameMap.getMap()[posYMap][posXMap]=0;
        //this.changeScoreSemaphore.release();
        //if(this.changeScoreSemaphore==null){
        //    Log.i("Change Score Semaphore","I'm null");
        //}else{
        //    Log.i("Change Score Semaphore","I'm not null");
        //}
    }

    public void eatBonus(int posXMap,int posYMap){
        SCORE+=500;
        CHANGE_SCORE_MUTEX.release();
        //this.score+=500;
        //Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;
        //this.changeScoreSemaphore.release();
    }

    public void eatSuperPallet(int posXMap,int posYMap){
        SCORE+=50;
        CHANGE_SCORE_MUTEX.release();
        //this.score+=50;
        this.gameMap.getMap()[posYMap][posXMap]=0;

        //Si hay un timer andando lo cancelo y ejecuto otro
        if (this.scareCountDown != null){
            this.scareCountDown.cancel();
        }
        this.scareCountDown = new CountDownScareGhosts(this.ghosts,this.gameMap.getMap());
        this.scareCountDown.start();
        //this.changeScoreSemaphore.release();
    }

    public void tryCreateBonus(){
        //only if pacman has eaten 20 pallets we should allow the fruit appear
        if(!this.fruitHasBeenInTheLevel && this.gameMap.getEatenPallets()>=20){
            //to not allow the fruit be again in the level
            this.fruitHasBeenInTheLevel=true;
            new CountdownBonusThread(this.gameMap,this.bonusResetTime).start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void moveGhosts(Canvas canvas,int blocksize) {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move(this.gameMap.getMap(),this.pacman);
            ghosts[i].draw(canvas);
        }
    }

    public synchronized void initGhosts(int blocksize, Resources res, String packageName,int movementFluency) {
        int[][]spawnPositions,cornersPositions, notUpDownPositions,defaultTargets;

        defaultTargets=this.gameMap.getDefaultGhostTarget();
        notUpDownPositions=this.gameMap.getNotUpDownDecisionPositions();
        spawnPositions=this.gameMap.getGhostsSpawnPositions();
        cornersPositions=this.gameMap.getGhostsScatterTarget();
        //start position
        // 5 blinky spawn [13, 11]
        // 6 pinky spawn [15,11]
        // 7 inky spawn [13,16]
        // 8 clyde spawn [15,16]
        this.ghosts=new Ghost[4];
        ghosts[0] = new Ghost("blinky",spawnPositions[0], cornersPositions[0] ,new BehaviorChaseAgressive(notUpDownPositions,movementFluency,defaultTargets[0]),movementFluency,notUpDownPositions,'l',defaultTargets[0],blocksize,res,packageName);
        ghosts[1] = new Ghost("pinky",spawnPositions[1],cornersPositions[1],new BehaviorChaseAmbush(notUpDownPositions,movementFluency,defaultTargets[1]),movementFluency,notUpDownPositions,'r',defaultTargets[1],blocksize,res,packageName);
        ghosts[2] = new Ghost("inky",spawnPositions[2],cornersPositions[2],new BehaviorChasePatrol(notUpDownPositions,this.ghosts[0],movementFluency,defaultTargets[0]),movementFluency,notUpDownPositions,'l',defaultTargets[0],blocksize,res,packageName);
        ghosts[3] = new Ghost("clyde",spawnPositions[3],cornersPositions[3],new BehaviorChaseRandom(notUpDownPositions,cornersPositions[3],movementFluency,defaultTargets[1]),movementFluency,notUpDownPositions,'r',defaultTargets[1],blocksize,res,packageName);

        try{
            Thread.sleep(200);
        }catch(Exception e){}

        for (int i=0;i<ghosts.length;i++){
            ghosts[i].onLevelStart(1);
        }

    }

    public boolean checkWinLevel() {
        //player win the level if he has eaten all the pallet
        return this.gameMap.countPallets()==0;
    }

    public void onResume(){
        for (int i=0 ; i<this.ghosts.length;i++){
            this.ghosts[i].cancelBehavoirThread();
        }
        if(this.scareCountDown!=null && !this.scareCountDown.hasEnded()){
            this.scareCountDown.start();
        }
    }

    public void onPause(){
        for (int i=0 ; i<this.ghosts.length;i++){
            this.ghosts[i].cancelBehavoirThread();
        }
        if(this.scareCountDown!=null && !this.scareCountDown.hasEnded()){
            this.scareCountDown=this.scareCountDown.onPause();
        }
    }

    public void cancelThreads(){
        for (int i=0 ; i<this.ghosts.length;i++){
            this.ghosts[i].cancelBehavoirThread();
        }
        if(this.scareCountDown!=null){
            this.scareCountDown.cancel();

        }
    }
}

//tes