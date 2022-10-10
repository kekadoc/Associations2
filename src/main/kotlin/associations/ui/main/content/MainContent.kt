@file:OptIn(ExperimentalMaterialApi::class)

package associations.ui.main.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import associations.common.collectAsState
import associations.common.collectSideEffect
import associations.common.ui.compose.layout.StaggeredLayout
import associations.ui.main.AppViewModel
import associations.ui.main.model.mvi.AppSideEffect
import associations.ui.main.model.mvi.AppViewState

@Composable
@Preview
fun MainContent(viewModel: AppViewModel) {
    
    val state: AppViewState by viewModel.collectAsState()
    
    var showAddingPropertyDialog by remember { mutableStateOf(false) }
    
    var showAddingPropertyValueDialog: Pair<String, String?>? by remember { mutableStateOf(null) }
    
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is AppSideEffect.ShowAddingProperty -> showAddingPropertyDialog = true
            is AppSideEffect.ShowAddingPropertyValue -> showAddingPropertyValueDialog = effect.property to effect.value
        }
    }
    
    if (showAddingPropertyDialog) {
        AddingPropertyDialog(
            onCancel = { showAddingPropertyDialog = false },
            onCommit = { property, value ->
                showAddingPropertyDialog = false
                viewModel.setPropertyValue(property, value)
            }
        )
    }
    
    showAddingPropertyValueDialog?.let { (property, value) ->
        AddingPropertyValueDialog(
            property = property,
            value = value.orEmpty(),
            onCancel = { showAddingPropertyValueDialog = null },
            onCommitValue = { newValue ->
                showAddingPropertyValueDialog = null
                viewModel.setPropertyValue(property, newValue)
            }
        )
    }
    
    MaterialTheme {
        Content(
            state = state,
            onSearchAction = viewModel::search,
            onBack = viewModel::back,
            onSearchValue = viewModel::searchValue,
            onValueChanged = viewModel::onValueChanged,
            onAddPropertyAction = viewModel::addPropertyAction,
            onAddPropertyValueAction = viewModel::addPropertyValueAction,
            onSetPropertyValueAction = viewModel::setPropertyValueAction,
            onDeleteProperty = viewModel::deleteProperty,
            onDeletePropertyValue = viewModel::deletePropertyValue
        )
    }
    
}

@Composable
fun AddingPropertyDialog(
    onCancel: () -> Unit,
    onCommit: (property: String, value: String) -> Unit,
) {
    var propertyText by remember { mutableStateOf("") }
    var valueText by remember { mutableStateOf("") }
    Dialog(
        onCloseRequest = onCancel,
        title = "Добавления свойства"
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = propertyText,
                    label = {
                        Text(text = "Свойство")
                    },
                    onValueChange = { propertyText = it }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = valueText,
                    label = {
                        Text(text = "Значение")
                    },
                    onValueChange = { valueText = it }
                )
            }
            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { onCommit.invoke(propertyText, valueText) },
                enabled = propertyText.isNotEmpty() && valueText.isNotEmpty()
            ) {
                Text(
                    text = "Добавить",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Composable
fun AddingPropertyValueDialog(
    value: String,
    property: String,
    onCancel: () -> Unit,
    onCommitValue: (String) -> Unit,
) {
    var valueText by remember { mutableStateOf(value) }
    Dialog(
        onCloseRequest = onCancel,
        title = property
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                value = valueText,
                label = {
                    Text(text = "Значение")
                },
                onValueChange = { valueText = it }
            )
            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { onCommitValue.invoke(valueText) },
                enabled = valueText.isNotEmpty()
            ) {
                Text(
                    text = "Добавить",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: AppViewState,
    onValueChanged: (String) -> Unit,
    onBack: () -> Unit,
    onSearchAction: () -> Unit,
    onSearchValue: (String) -> Unit,
    onAddPropertyAction: () -> Unit,
    onAddPropertyValueAction: (String) -> Unit,
    onSetPropertyValueAction: (String, String) -> Unit,
    onDeletePropertyValue: (String, String) -> Unit,
    onDeleteProperty: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                modifier = Modifier.size(44.dp).align(Alignment.CenterVertically),
                onClick = onBack,
                enabled = state.hasPreviewElement,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    modifier = Modifier,
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                )
            }
            OutlinedTextField(
                modifier = Modifier,
                value = state.searchValue,
                onValueChange = { onValueChanged.invoke(it) },
                label = {
                    Text("Сущность")
                }
            )
            Button(
                modifier = Modifier.size(44.dp).align(Alignment.CenterVertically),
                onClick = onSearchAction,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    modifier = Modifier,
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                )
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        state.currentElement?.let { element ->
            element.parts.forEach { (property, values) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onDeleteProperty.invoke(property) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = property,
                        style = MaterialTheme.typography.h6
                    )
                }
                StaggeredLayout(
                    modifier = Modifier.wrapContentHeight().padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Arrangement.spacedBy(8.dp)
                ) {
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        values.forEach { value ->
                            ValueCompoenent(
                                value = value,
                                onClick = { onSearchValue.invoke(value) },
                                onEdit = { onSetPropertyValueAction.invoke(property, value) },
                                onDelete = { onDeletePropertyValue.invoke(property, value) }
                            )
                        }
                        Card(
                            modifier = Modifier.size(36.dp),
                            elevation = 3.dp,
                            backgroundColor = MaterialTheme.colors.primary,
                            onClick = { onAddPropertyValueAction.invoke(property) }
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                            )
                        }
                    }
                }
                Divider()
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Button(onClick = { onAddPropertyAction.invoke() }) {
                    Text("Добавить свойство")
                }
            }
        }
    }
}

@Composable
private fun ValueCompoenent(
    value: String,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.wrapContentSize().height(36.dp),
        elevation = 3.dp,
        onClick = { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier.wrapContentWidth().padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp).height(24.dp),
                text = value,
                style = MaterialTheme.typography.h6
            )
            Button(
                modifier = Modifier.size(24.dp),
                onClick = onEdit::invoke,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                )
            }
            Button(
                modifier = Modifier.size(24.dp),
                onClick = onDelete::invoke,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                )
            }
        }
    }
}