
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
			&times;
		</button>
		<h3>添加服务提供者</h3>
	</div><!-- /.modal-header -->
	
	<form id="J_FORM_ADD_PROVIDER_STEP_1" action="${base}/console/provider/ajax/edit">
		
		<div class="modal-body">

			<div class="radio-list-wrapper">

				<ul class="nav">
					<li>
						<div class="radio-item-wrapper">
							<label for="own1">
								<input id="own1" type="radio" name="serviceType" value="localTile" checked/>
								arcgis静态切片</label>
							<span class="radio-desc">通过读取arcgis切片提供服务</span>
						</div>
					</li>
					<li>
						<div class="radio-item-wrapper">
							<label for="own2">
								<input id="own2" type="radio" name="serviceType" value="arcgisProxy"/>
								arcgis代理</label>
							<span class="radio-desc">代理已有的arcgis服务</span>
						</div>
					</li>
					<li>
						<div class="radio-item-wrapper">
							<label for="own3">
								<input id="own3" type="radio" name="serviceType" value="index"/>
								索引服务</label>
							<span class="radio-desc">通过制作索引提供检索服务</span>
						</div>
					</li>
					<li>
						<div class="radio-item-wrapper">
							<label for="own4">
								<input id="own4" type="radio" name="serviceType" value="datasource"/>
								数据源直接检索</label>
							<span class="radio-desc">直接通过数据源检索</span>
						</div>
					</li>
                    <li>
                        <div class="radio-item-wrapper">
                            <label for="own5">
                                <input id="own5" type="radio" name="serviceType" value="wmtsProxy"/>
                                wmts代理</label>
                            <span class="radio-desc">代理已有的wmts服务</span>
                        </div>
                    </li>
				</ul>
			</div><!-- /.provider-list-wrapper -->
			
		</div>
		<div class="modal-footer">
			<#if mapId??><input type="hidden" name="mapId" value="${mapId}"/></#if>
			<a id="J_BTN_ADD_PROVIDER_NEXT" href="#J_NEW_PROVIDER_MODAL_2" data-toggle="modal" class="btn btn-primary" data-dismiss="modal" aria-hidden="true">下一步</a>
		</div>
	</form>
