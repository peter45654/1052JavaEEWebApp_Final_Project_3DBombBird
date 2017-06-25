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


		</style>
	</head>
	<body>
		<script type="text/javascript" src="lib/three.min.js" ></script>
    <script src="lib/Detector.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script type="text/javascript" src="lib/MTLLoader.js" ></script>
		<script type="text/javascript" src="lib/OBJLoader.js" ></script>
		<script src="js/bomb.js"></script>
		<script src="js/Player.js"></script>
		<script src="js/Crate.js"></script>
		<script src="js/Rock.js"></script>
		<script src="js/Map.js"></script>
		<script src="lib/Fire.js"></script>
		<script src="lib/FireShader.js"></script>
		<script src="./three.js - pointerlock controls_files/PointerLockControls.js"></script>

		<script>
			var collidableObjects=[];//player會撞到的物件
			var rockObjects=[];//不能破壞的東西的物件
			var CrateObjects=[];//可以破壞的道具的物件
			var rockMesh=[];//不能破壞的東西的Mesh
			var CrateMesh=[];//可以破壞的道具的Mesh
			var PLAYERCOLLISIONDISTANCE = 1;//玩家離多遠算撞牆
			var UNITWIDTH=5;//一個格子的寬度
			var mapSize=0;//地圖大小
			var totalCubesWide=0;//cube數量
			var camera, scene, renderer;//鏡頭,場景,渲染器
			var player;//
			var player2;//
			var fires=[];//火焰的陣列
			var models = {
          bomb:{
                        obj:"models/bomb.obj",
                        mtl:"models/bomb.mtl",
                        mesh:null
          }
				}
			init();
			function init() {
						if ( ! Detector.webgl ) Detector.addGetWebGLMessage();//如果沒GL就載入
						camera = new THREE.PerspectiveCamera( 35, window.innerWidth / window.innerHeight, 1, 500 );
						scene = new THREE.Scene();							  //場景
						renderer = new THREE.WebGLRenderer({antialias: true});//渲染器
						renderer.setSize( window.innerWidth, window.innerHeight );//渲染範圍
						document.body.appendChild( renderer.domElement );
						clock = new THREE.Clock();//時脈
						camera.position.set(0,50, 0);
						camera.lookAt(new THREE.Vector3(0,0,0));
						var light = new THREE.HemisphereLight( 0xeeeeff, 0x777788, 0.75 );
						light.position.set( 0.5, 1, 0.75 );
						scene.add( light );
						var mtlLoader = new THREE.MTLLoader();//載入炸彈模型
						mtlLoader.load(models.bomb.mtl, function(materials){
		                 materials.preload();
		                 var objLoader = new THREE.OBJLoader();
		                 objLoader.setMaterials(materials);
		                 objLoader.load(models.bomb.obj, function(mesh){
		                        models.bomb.mesh = mesh;
		                        });
													});
						var loader = new THREE.JSONLoader();//載入玩家模型
						loader.load( "models/parrot.json", function( geometry ) {
							var material = new THREE.MeshPhongMaterial( {
								color: 0xffffff,
								morphTargets: true,
								vertexColors: THREE.FaceColors,
								shading: THREE.FlatShading
							} );
						var temp = new THREE.Mesh( geometry, material );
						player=new Player(temp);
						player2=new Player(temp.clone());
						player2.mesh.position.set(30.5,2.5,30.5);
						scene.add( player.mesh);
						scene.add( player2.mesh);
						mixer = new THREE.AnimationMixer( player.mesh );
						mixer.clipAction( player.mesh.geometry.animations[ 0 ] ).setDuration( 1 ).play();
						callback();
						} );
    }
			function callback()
			{
							createMap();//建立地圖
							wallLoad();//載入牆壁
							floorLoad();//載入地板
							drawSimpleSkybox();//載入天空
							animate();
			}
			function wallLoad(){
				var wallGeometry=new THREE.PlaneGeometry( 100, 15, 50, 50 );
				var wallTexture = new THREE.ImageUtils.loadTexture( 'texture/wall.jpg' );
				wallTexture.wrapS = wallTexture.wrapT = THREE.RepeatWrapping;
				wallTexture.repeat.set( 50, 10 );
				var wallMaterial = new THREE.MeshPhongMaterial( { map: wallTexture, side: THREE.DoubleSide } );
			  var wall=new THREE.Mesh( wallGeometry, wallMaterial );
				wall.alive=true;
				wall.rotateY( - Math.PI / 2 );
				wall.position.set(mapSize,0,mapSize/2);
				var wall2=new THREE.Mesh( wallGeometry, wallMaterial );
				wall2.alive=true;
				wall2.rotateY( + Math.PI / 2 );
				wall2.position.set(0,0,mapSize/2);
				var wall3=new THREE.Mesh( wallGeometry, wallMaterial );
				wall3.alive=true;
				wall3.position.set(mapSize/2,0,0);
				var wall4=new THREE.Mesh( wallGeometry, wallMaterial );
				wall4.alive=true;
				wall4.position.set(mapSize/2,0,mapSize);
				scene.add(wall);
				scene.add(wall2);
				scene.add(wall3);
				scene.add(wall4);
				collidableObjects.push(wall);
				collidableObjects.push(wall2);
				collidableObjects.push(wall3);
				collidableObjects.push(wall4);
				rockMesh.push(wall);
				rockMesh.push(wall2);
				rockMesh.push(wall3);
				rockMesh.push(wall4);	
			}
			function onWindowResize() {
				camera.aspect = window.innerWidth / window.innerHeight;
				camera.updateProjectionMatrix();
				renderer.setSize( window.innerWidth, window.innerHeight );
			}
			function floorLoad(){
				var floorTexture = new THREE.ImageUtils.loadTexture( 'texture/floor.jpg' );
				floorTexture.wrapS = floorTexture.wrapT = THREE.RepeatWrapping;
				floorTexture.repeat.set( 70, 70 );
				var floorMaterial = new THREE.MeshBasicMaterial( { map: floorTexture, side: THREE.DoubleSide } );
				var floorGeometry = new THREE.PlaneGeometry(mapSize, mapSize, 10, 10);
				var floor = new THREE.Mesh(floorGeometry, floorMaterial);
				floor.position.set(mapSize/2,0,mapSize/2);
				floor.rotation.x = Math.PI / 2;
				scene.add(floor);
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
			skyBox = new THREE.Mesh(new THREE.CubeGeometry(300, 300, 300), skyMaterial);
			this.scene.add(skyBox);
			}

			function moveControl(){

                if(player.move.down == true){ // S key
                    player.mesh.rotation.y = 0;
										if(!player.detectPlayerCollision())
                    player.mesh.position.z += Math.cos(player.mesh.rotation.y) * 0.3;
                }
                if(player.move.up == true){ // W key
                    player.mesh.rotation.y = Math.PI;
										if(!player.detectPlayerCollision())
                    player.mesh.position.z += Math.cos(player.mesh.rotation.y) * 0.3;

                }
                if(player.move.right == true){ // D key
                    player.mesh.rotation.y = Math.PI/2;
										if(!player.detectPlayerCollision())
                    player.mesh.position.x += Math.sin(player.mesh.rotation.y) * 0.3;

                }
                if(player.move.left == true){ // A key
                    player.mesh.rotation.y = Math.PI*3/2
										if(!player.detectPlayerCollision())
                    player.mesh.position.x += Math.sin(player.mesh.rotation.y) * 0.3;

                }

                if(player.move.atk){
                  player.Attack();
                }
                try{
                camera.position.z=player.mesh.position.z + 50;
                //camera.position.y=player.position.y=100;
                camera.position.x=player.mesh.position.x;
                camera.lookAt(new THREE.Vector3(player.mesh.position.x,5,player.mesh.position.z));
                }catch(e){}

			}


			function animate() {

				requestAnimationFrame( animate );
				var time = Date.now() * 0.0005;
				var delta = clock.getDelta();
				collidableObjectsCheck();//Map.js 裡面
				bombsCheck();
				firesCheck();
				moveControl();
				if(mixer)mixer.update( delta );
				if(fires){
					for(var i=0;i<fires.length;i++)
					fires[i].update(clock.elapsedTime);
			}
			//畫東西
				renderer.render( scene, camera );

			}
			function keyDown(event){
				switch (event.keyCode) {
		      case 38: // up
		      case 87: // w
		        player.move.up = true;
						player.move.left = false;
						player.move.down = false;
						player.move.right = false;
		        break;

		      case 37: // left
		      case 65: // a
		         player.move.left = true;
						 player.move.up = false;
						 player.move.down = false;
						 player.move.right = false;
		        break;

		      case 40: // down
		      case 83: // s
		         player.move.down = true;
						 player.move.left = false;
						 player.move.up = false;
						 player.move.right = false;
		        break;

		      case 39: // right
		      case 68: // d
		         player.move.right = true;
						 player.move.left = false;
						 player.move.down = false;
						 player.move.up = false;
		        break;

					case 32: // space
						//alert(fires.length);
						player.move.atk = true;
						break;
    			}
			}

			function keyUp(event){
				switch (event.keyCode) {
		      case 38: // up
		      case 87: // w
		        player.move.up = false;
		        break;

		      case 37: // left
		      case 65: // a
		         player.move.left = false;
		        break;

		      case 40: // down
		      case 83: // s
		         player.move.down = false;
		        break;

		      case 39: // right
		      case 68: // d
		         player.move.right = false;
		        break;
					case 32: // d
							player.move.atk = false;
							break;
    			}
			}


			window.addEventListener('keydown', keyDown);
			window.addEventListener('keyup', keyUp);
			window.addEventListener( 'resize', onWindowResize, false );
		</script>

</body></html>
