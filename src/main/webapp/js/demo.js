var scene, camera, renderer, mesh, clock,controls;
var skyBox;
var meshFloor, ambientLight, light;
var keyboard = {};
var loadingScreen = {
	scene: new THREE.Scene(),
	camera: new THREE.PerspectiveCamera(90, 1280/720, 0.1, 100)
};
var loadingManager = null;
var RESOURCES_LOADED = false;

// Models index
var models = {
	
};

// Meshes index
var meshes = {};

var character;
start();
function start(){
	
	scene = new THREE.Scene();//產生場景
	camera = new THREE.PerspectiveCamera(90, innerWidth/innerHeight, 0.1, 1000);//產生相機
	clock = new THREE.Clock();//時脈
	scene.fog = new THREE.Fog( 0xcce0ff, 500, 10000 );//場景顏色

	character  =new THREE.Mesh(
		new THREE.BoxGeometry( 1, 1, 1 ,10,10,10),
		new THREE.MeshPhongMaterial({wireframe:false})
	);
	controls = new THREE.PointerLockControls( camera );
	scene.add( controls.getObject() );
	character.position.set(0,0,0);
	scene.add(character);
	floorLoad();
	lightLoad();
	drawSimpleSkybox();
	camera.position.set(0, 5, -5);
	//camera.lookAt(character);
	
	renderer = new THREE.WebGLRenderer();
	renderer.setSize(innerWidth, innerHeight-20);
	renderer.shadowMap.enabled = true;
	renderer.shadowMap.type = THREE.BasicShadowMap;

	document.body.appendChild(renderer.domElement);
	
	animate();
    }

function floorLoad(){
	var floorTexture = new THREE.ImageUtils.loadTexture( 'floor.jpg' );
	floorTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping; 
	floorTexture.repeat.set( 50, 50 );
	var floorMaterial = new THREE.MeshBasicMaterial( { map: floorTexture, side: THREE.DoubleSide } );
	var floorGeometry = new THREE.PlaneGeometry(500, 500, 10, 10);
	var floor = new THREE.Mesh(floorGeometry, floorMaterial);
	floor.position.y = -0.5;
	floor.rotation.x = Math.PI / 2;
	scene.add(floor);
}
function lightLoad(){
	ambientLight = new THREE.AmbientLight( 0x666666,0.8 );
	scene.add(ambientLight);
	light = new THREE.PointLight(0xffffff, 1, 18);
	light.position.set(camera.position.x,6,camera.position.z);
	light.castShadow = true;
	light.shadow.camera.near = 0.1;
	light.shadow.camera.far = 25;
	scene.add(light);
}
function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize( window.innerWidth, window.innerHeight );
    }

function drawSimpleSkybox() {
		var path = 'skybox/';
		var sides = [ path + 'sbox_px.jpg', path + 'sbox_nx.jpg', path + 'sbox_py.jpg', path + 'sbox_ny.jpg', path + 'sbox_pz.jpg', path + 'sbox_nz.jpg' ];
		var scCube = THREE.ImageUtils.loadTextureCube(sides);
		var skyShader = THREE.ShaderLib["cube"];
		skyShader.uniforms["tCube"].value = scCube;
		var skyMaterial = new THREE.ShaderMaterial( {
		fragmentShader: skyShader.fragmentShader, vertexShader: skyShader.vertexShader,
		uniforms: skyShader.uniforms, depthWrite: false, side: THREE.BackSide
		});
		skyBox = new THREE.Mesh(new THREE.CubeGeometry(500, 500, 500), skyMaterial);
		this.scene.add(skyBox);
}


function moveControl(){
	if(keyboard[87]){ // W key
		character.position.x -= Math.sin(character.rotation.y) * 0.2;
		character.position.z -= -Math.cos(character.rotation.y) * 0.2;
	}
	if(keyboard[83]){ // S key
		character.position.x += Math.sin(character.rotation.y) * 0.2;
		character.position.z += -Math.cos(character.rotation.y) * 0.2;
	}
	if(keyboard[65]){ // A key
		character.position.x += Math.sin(character.rotation.y + Math.PI/2) * 0.2;
		character.position.z += -Math.cos(character.rotation.y + Math.PI/2) * 0.2;
	}
	if(keyboard[68]){ // D key
		character.position.x += Math.sin(character.rotation.y - Math.PI/2) * 0.2;
		character.position.z += -Math.cos(character.rotation.y - Math.PI/2) * 0.2;
	}
	if(keyboard[37]){ // left arrow key
		character.rotation.y -= 0.2;
	}
	if(keyboard[39]){ // right arrow key
		character.rotation.y += 0.2;
	}
	
}


function animate(){
	//orbitcontrols.update();
	light.position.set(camera.position.x,6,camera.position.z);
	var time = Date.now() * 0.0005;
	var delta = clock.getDelta();
	//
	camera.position.set(character.position.x, character.position.y+10, character.position.z);
	//camera.lookAt(character);
	moveControl();
	renderer.render(scene, camera);
	requestAnimationFrame(animate);
}

function keyDown(event){
	keyboard[event.keyCode] = true;
}

function keyUp(event){
	keyboard[event.keyCode] = false;
}

window.addEventListener('keydown', keyDown);
window.addEventListener('keyup', keyUp);
window.addEventListener( 'resize', onWindowResize, false );


