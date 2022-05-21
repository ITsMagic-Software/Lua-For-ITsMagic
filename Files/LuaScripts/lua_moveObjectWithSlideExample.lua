function start() 
speed = 5
end 

function update() 
slide = Input:getMaxSlide()
right = slide:getX() * speed
up = -slide:getY() * speed

myObject:getTransform():moveInSeconds(right,0,up)
end

function disabledUpdate()

end
