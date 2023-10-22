from cmu_graphics import *

app.grid = makeList(1000,1000)
app.rects = []
app.markers = []
app.setMaxShapeCount(5000)

# Cellular automata simulation: Conways game of life
# If a cell is dead and has exactly 3 living neighbors, it comes alive 
# If a cell is alive and has 2 or 3 neighors, it stays alive
# Otherwise it dies 

class cells_game():
    def __init__(self, *cells):   # Format ([col, row], [col, row], [col, row])  x,y
        self.cells = list(cells)
        # for cell in self.cells:
        #     app.rects.append(Rect(cell[0]*4, cell[1]*4, 4,4, fill="Black"))
        #     app.grid[cell[0]][cell[1]] = True
    
    def search_area(self, point=None):
        if point == None:
            return [[[(cell.centerX-2)//4+1,(cell.centerY-2)//4], 
            [(cell.centerX-2)//4-1,(cell.centerY-2)//4], 
            [(cell.centerX-2)//4+1,(cell.centerY-2)//4+1], 
            [(cell.centerX-2)//4+1,(cell.centerY-2)//4-1], 
            [(cell.centerX-2)//4-1,(cell.centerY-2)//4-1], 
            [(cell.centerX-2)//4-1,(cell.centerY-2)//4+1], 
            [(cell.centerX-2)//4,(cell.centerY-2)//4-1], 
            [(cell.centerX-2)//4,(cell.centerY-2)//4+1]] for cell in app.rects]   # Format [[square_coordinates]]
        else:
            return [[point[0]+1, 
            point[1]], 
            [point[0]-1,point[1]], 
            [point[0]+1,point[1]+1], 
            [point[0]+1,point[1]-1], 
            [point[0]-1,point[1]-1], 
            [point[0]-1,point[1]+1], 
            [point[0],point[1]-1], 
            [point[0],point[1]+1]]
        
    # def return_living_cells(self):
    #     return [[(cell.centerX-2%4)//4,(cell.centerY-2%4)//4] for cell in app.rects]
        
    def dead_cells(self):   #  If a cell is dead and has exactly 3 living neighbors, it comes alive
        search_zone = self.search_area()
        add = []
            
        for zone in range(len(search_zone)):
            for cell in search_zone[zone]:
                if app.grid[cell[0]][cell[1]] == None:
                    cell_search_area = self.search_area(point=cell)
                        
                    living = 0
                    for cell_zone in cell_search_area:
                        if app.grid[cell_zone[0]][cell_zone[1]] == True:
                            living += 1 
                    
                    if living == 3:
                        add.append(cell)
                        app.grid[cell[0]][cell[1]] = False
                
        for zone in range(len(search_zone)):
            for cell in search_zone[zone]:
                if app.grid[cell[0]][cell[1]] == False:
                    app.grid[cell[0]][cell[1]] == None
        return add
                    
    def living_cells(self):   # If a cell is alive and has 2 or 3 neighors, it stays alive otherwise it dies
        remove = []
        for living_cell in app.rects:
            living = 0
            for cell in self.search_area(point=[living_cell.centerX//4, living_cell.centerY//4]):
                if app.grid[cell[0]][cell[1]] == True:
                    living += 1 
                        
            if not (living == 2 or living == 3):
                remove.append(living_cell)
        return remove
    
    def update(self, add, remove):
        for cell in add:
            app.rects.append(Rect(cell[0]*4, cell[1]*4, 4,4, fill="Black"))
            app.grid[cell[0]][cell[1]] = True
        
        # print(len(remove), "remove")    
        # print(len(app.rects), "before")
        for cell in remove:
            cell.visible = False
            app.grid[cell.centerX//4][cell.centerY//4] = None
            app.rects.remove(cell)
        # print(len(app.rects), "after")
    
    def new_initial_state(self, cells, rects=False):
        self.cells = list(cells)
        if rects == True:
            for cell in self.cells:
                app.rects.append(Rect(cell[0]*4, cell[1]*4, 4,4, fill="Black"))
                app.grid[cell[0]][cell[1]] = True

    
    def add_marker(self, cells):
        if cells != None:
            for cell in cells:
                if app.grid[cell[0]][cell[1]] == None:
                    app.markers.append(Rect(cell[0]*4, cell[1]*4, 4,4, fill="Green"))

# a = cells_game()
app.start = False
app.initial_state = []

def onMousePress(x,y):
    if app.start == False: 
        app.initial_state.append([x//4, y//4])
        app.rects.append(Rect(x-x%4, y-y%4, 4,4, fill="Black"))
        app.grid[x//4][y//4] = True
        
app.a = cells_game(None)
app.stepsPerSecond = 15
def onStep():
    if app.start == True:
        app.a.update(app.a.dead_cells(),app.a.living_cells())
def clear_state():
        for rect in app.rects:
            rect.visible = False
        app.rects.clear()
        app.grid = makeList(1000,1000)
        app.start = False
        app.initial_state.clear()
        
def onKeyPress(key):
    if key == "space" and app.start == False:
        app.start = not app.start
        print("start")
        app.a.new_initial_state(app.initial_state)
    if key.lower() == "c":
        clear_state()
    if key.lower() == "g":
        clear_state()
        app.a.new_initial_state(
                            [[1,5],[2,5],[1,6],[2,6],
                [11,5], [11,6], [11,7], [12,4], [12,8],[13,3],[13,9],[14,3],[14,9],
                [15,6], [16,4], [16,8], [17,5], [17,6], [17,7], [18,6],
                [21,3], [21,4], [21,5], [22,3], [22,4], [22,5], [23,2], [23,6], [25,1], [25,2],
                [25,6], [25,7], [35,3], [35,4], [36,3], [36,4]], True
            )

# [40,40], [40,42],[41,41],[41,42],[42,41] 

                # [1,5],[2,5],[1,6],[2,6],
                # [11,5], [11,6], [11,7], [12,4], [12,8],[13,3],[13,9],[14,3],[14,9],
                # [15,6], [16,4], [16,8], [17,5], [17,6], [17,7], [18,6],
                # [21,3], [21,4], [21,5], [22,3], [22,4], [22,5], [23,2], [23,6], [25,1], [25,2],
                # [25,6], [25,7], [35,3], [35,4], [36,3], [36,4]
                
# search_area = a.search_area()
# dead_cells = a.dead_cells()
# print(dead_cells)
#a.add_marker(search_area[0])
# print(search_area)
# print()
# print(search_area[0])
# print()
# print(search_area[1])

cmu_graphics.run()

