<!DOCTYPE html>
<!-- saved from url=(0059)https://threejs.org/examples/misc_controls_pointerlock.html -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>three.js - pointerlock controls</title>

		<meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<style>
			html, body {
				width: 100%;
				height: 100%;
			}

			body {
				background-color: #ffffff;
				margin: 0;
				overflow: hidden;
				font-family: arial;
			}

			#blocker {

				position: absolute;

				width: 100%;
				height: 100%;

				background-color: rgba(0,0,0,0.5);

			}

			#instructions {

				width: 100%;
				height: 100%;

				display: -webkit-box;
				display: -moz-box;
				display: box;

				-webkit-box-orient: horizontal;
				-moz-box-orient: horizontal;
				box-orient: horizontal;

				-webkit-box-pack: center;
				-moz-box-pack: center;
				box-pack: center;

				-webkit-box-align: center;
				-moz-box-align: center;
				box-align: center;

				color: #ffffff;
				text-align: center;

				cursor: pointer;

			}

		</style>
	</head>
	<body>
		<script type="text/javascript" src="js/three.min.js" ></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script type="text/javascript" src="js/MTLLoader.js" ></script>
		<script type="text/javascript" src="js/OBJLoader.js" ></script>
		<script src="./three.js - pointerlock controls_files/PointerLockControls.js"></script>

		<div id="blocker" style="display: -webkit-box;">

			<div id="instructions">
				<span style="font-size:40px">Click to play</span>
				<br>
				(W, A, S, D = Move, SPACE = Jump, MOUSE = Look around)
			</div>

		</div>

		<script>

			var camera, scene, renderer;
			var geometry, material, floor;
			var controls;

			var objects = [];

			var raycaster;

			var blocker = document.getElementById( 'blocker' );
			var instructions = document.getElementById( 'instructions' );


			var havePointerLock = 'pointerLockElement' in document || 'mozPointerLockElement' in document || 'webkitPointerLockElement' in document;

			if ( havePointerLock ) {

				var element = document.body;

				var pointerlockchange = function ( event ) {

					if ( document.pointerLockElement === element || document.mozPointerLockElement === element || document.webkitPointerLockElement === element ) {

						controlsEnabled = true;
						controls.enabled = true;

						blocker.style.display = 'none';

					} else {

						controls.enabled = false;

						blocker.style.display = '-webkit-box';
						blocker.style.display = '-moz-box';
						blocker.style.display = 'box';

						instructions.style.display = '';

					}

				};

				var pointerlockerror = function ( event ) {

					instructions.style.display = '';

				};

				// Hook pointer lock state change events
				document.addEventListener( 'pointerlockchange', pointerlockchange, false );
				document.addEventListener( 'mozpointerlockchange', pointerlockchange, false );
				document.addEventListener( 'webkitpointerlockchange', pointerlockchange, false );

				document.addEventListener( 'pointerlockerror', pointerlockerror, false );
				document.addEventListener( 'mozpointerlockerror', pointerlockerror, false );
				document.addEventListener( 'webkitpointerlockerror', pointerlockerror, false );

				instructions.addEventListener( 'click', function ( event ) {
					instructions.style.display = 'none';
					element.requestPointerLock = element.requestPointerLock || element.mozRequestPointerLock || element.webkitRequestPointerLock;
					element.requestPointerLock();

				}, false );

			} else {
				instructions.innerHTML = 'Your browser doesn\'t seem to support Pointer Lock API';
			}

			init();
			animate();

			var controlsEnabled = false;
			var flamingo;
			var moveForward = false;
			var moveBackward = false;
			var moveLeft = false;
			var moveRight = false;
			var canJump = false;
			var mixer;
			var prevTime = performance.now();
			var velocity = new THREE.Vector3();

			function init() {

				camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 1, 3000 );
				camera.position.y=200;
				scene = new THREE.Scene();
				scene.fog = new THREE.Fog( 0xffffff, 0, 750 );

				controls = new THREE.PointerLockControls( camera );
				scene.add( controls.getObject() );

				var loader = new THREE.JSONLoader();
				loader.load( "models/parrot.json", function( geometry ) {

					var material = new THREE.MeshPhongMaterial( {
						color: 0xffffff,
						morphTargets: true,
						vertexColors: THREE.FaceColors,
						shading: THREE.FlatShading
					} );
					flamingo = new THREE.Mesh( geometry, material );
					flamingo.scale.set( 0.01, 0.01, 0.01 );
					flamingo.position.y=10;
					scene.add( flamingo );

					mixer = new THREE.AnimationMixer( flamingo );
					mixer.clipAction( geometry.animations[ 0 ] ).setDuration( 1 ).play();
				} );


				raycaster = new THREE.Raycaster( new THREE.Vector3(), new THREE.Vector3( 0, - 1, 0 ), 0, 10 );

				geometry = new THREE.PlaneGeometry( 2000, 2000, 100, 100 );
				geometry.rotateX( - Math.PI / 2 );
				var floorTexture = new THREE.ImageUtils.loadTexture( 'texture/floor.jpg' );
				floorTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping;
				floorTexture.repeat.set( 50, 50 );
				var floorMaterial = new THREE.MeshBasicMaterial( { map: floorTexture, side: THREE.DoubleSide } );
				material = new THREE.MeshBasicMaterial( { vertexColors: THREE.VertexColors } );
				floor = new THREE.Mesh( geometry, floorMaterial );


				var wallGeometry=new THREE.PlaneGeometry( 500, 100, 100, 100 );
				var wallTexture = new THREE.ImageUtils.loadTexture( 'texture/wall.jpg' );
				wallTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping;
				wallTexture.repeat.set( 5, 1 );
				var wallMaterial = new THREE.MeshBasicMaterial( { map: wallTexture, side: THREE.DoubleSide } );

			  var wall=new THREE.Mesh( wallGeometry, wallMaterial );
				wall.rotateY( - Math.PI / 2 );
				wall.position.x=250;
				var wall2=new THREE.Mesh( wallGeometry, wallMaterial );
				wall2.rotateY( + Math.PI / 2 );
				wall2.position.x=-250;
				var wall3=new THREE.Mesh( wallGeometry, wallMaterial );
				wall3.position.z=250;
				var wall4=new THREE.Mesh( wallGeometry, wallMaterial );
				wall4.position.z=-250;
				scene.add(wall);
				scene.add(wall2);
				scene.add(wall3);
				scene.add(wall4);
				scene.add(floor);

				drawSimpleSkybox();
				renderer = new THREE.WebGLRenderer();
				//renderer.setClearColor( 0xffffff );
				renderer.setPixelRatio( window.devicePixelRatio );
				renderer.setSize( window.innerWidth, window.innerHeight );
				document.body.appendChild( renderer.domElement );

				//

				window.addEventListener( 'resize', onWindowResize, false );

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
			skyBox = new THREE.Mesh(new THREE.CubeGeometry(1000, 1000,1000), skyMaterial);
			this.scene.add(skyBox);
			}

			function animate() {

				requestAnimationFrame( animate );

				if ( controlsEnabled ) {
					raycaster.ray.origin.copy( controls.getObject().position );
					raycaster.ray.origin.y -= 10;

					var intersections = raycaster.intersectObjects( objects );

					var isOnObject = intersections.length > 0;



				

		
					prevTime = time;

				}

				renderer.render( scene, camera );

			}

			var onKeyDown = function ( event ) {

					switch ( event.keyCode ) {

						case 38: // up
						case 87: // w
							moveForward = true;
							break;

						case 37: // left
						case 65: // a
							moveLeft = true; break;

						case 40: // down
						case 83: // s
							moveBackward = true;
							break;

						case 39: // right
						case 68: // d
							moveRight = true;
							break;

						case 69: // space
							if ( canJump === true ) velocity.y += 350;
							canJump = false;
							break;

					}

				};

				var onKeyUp = function ( event ) {

					switch( event.keyCode ) {

						case 38: // up
						case 87: // w
							moveForward = false;
							break;

						case 37: // left
						case 65: // a
							moveLeft = false;
							break;

						case 40: // down
						case 83: // s
							moveBackward = false;
							break;

						case 39: // right
						case 68: // d
							moveRight = false;
							break;

					}

				};

				document.addEventListener( 'keydown', onKeyDown, false );
				document.addEventListener( 'keyup', onKeyUp, false );

		</script><canvas width="187" height="858" style="width: 150px; height: 687px;"></canvas>


</body></html>
