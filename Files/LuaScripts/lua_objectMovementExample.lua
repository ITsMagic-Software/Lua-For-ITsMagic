function start() 
speed = 10
end 

function update() 
myObject:getTransform():moveInSeconds(speed,0,0)
end

function disabledUpdate()

end
